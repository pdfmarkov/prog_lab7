package com.markovpetr.command.commands;

import com.markovpetr.command.commands.exceptions.*;
import com.markovpetr.command.entity.Coordinates;
import com.markovpetr.command.entity.Location;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.User;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database {
	private Connection connection;
	private Statement statement;

	public Database(String login, String password) throws ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", login, password);
			statement = connection.createStatement();
//			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insert(User user) throws SQLException, NotDatabaseUpdateException {
		PreparedStatement preparedStatement =
						connection.prepareStatement("insert into users (login, password) values (?, ?) returning id");
		preparedStatement.setString(1, user.getLogin());
		preparedStatement.setString(2, user.getPassword());

		try {
			if (preparedStatement.execute()) {
				ResultSet rs = preparedStatement.getResultSet();
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
			throw new NotDatabaseUpdateException("Объект user не был добавлен");
		} catch (PSQLException e) {
			System.out.println(e.getMessage());
			throw new NotDatabaseUpdateException("Объект user не был добавлен");
		}
	}

	public int insert(Person person, int userID) throws SQLException, NotDatabaseUpdateException {
		int locationID = insert(person.getLocation());
		int coordsID = insert(person.getCoordinates());

		PreparedStatement preparedStatement =
						connection.prepareStatement("insert into persons (name, coordinates_id, creationdate, " +
										"height, passport, haircolor, nationality, location_id, user_id) " +
										"values (?, ?, ?, ?, ?, ?, ?, ?, ?) returning id");
		preparedStatement.setString(1, person.getName());
		preparedStatement.setInt(2, coordsID);
		preparedStatement.setTimestamp(3, Timestamp.valueOf(person.getCreationDate()));
		preparedStatement.setDouble(4, person.getHeight());
		preparedStatement.setString(5, person.getPassportID());
		preparedStatement.setString(6, person.getHairColor().toString());
		preparedStatement.setString(7, person.getNationality().toString());
		preparedStatement.setInt(8, locationID);
		preparedStatement.setInt(9, userID);

		try {
			if (preparedStatement.execute()) {
				ResultSet rs = preparedStatement.getResultSet();
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
			throw new NotDatabaseUpdateException("Объект person не был добавлен");
		} catch (PSQLException e) {
			System.err.println(e.getMessage());
			throw new NotDatabaseUpdateException("Объект person не был добавлен");
		}
	}

	private int insert(Location location) throws SQLException, NotDatabaseUpdateException {
		PreparedStatement preparedStatement =
						connection.prepareStatement("insert into locations (x, y, name) values (?, ?, ?) returning id");
		preparedStatement.setLong(1, location.getX());
		preparedStatement.setInt(2, location.getY());
		preparedStatement.setString(3, location.getName());

		try {
			if (preparedStatement.execute()) {
				ResultSet rs = preparedStatement.getResultSet();
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
			throw new NotDatabaseUpdateException("Объект location не был добавлен");
		} catch (PSQLException e) {
			System.out.println(e.getMessage());
			throw new NotDatabaseUpdateException("Объект location не был добавлен");
		}
	}

	private int insert(Coordinates coords) throws SQLException, NotDatabaseUpdateException {
		PreparedStatement preparedStatement =
						connection.prepareStatement("insert into coordinates (x, y) values (?, ?) returning id");
		preparedStatement.setDouble(1, coords.getX());
		preparedStatement.setFloat(2, coords.getY());

		try {
			if (preparedStatement.execute()) {
				ResultSet rs = preparedStatement.getResultSet();
				if (rs.next()) {
					return rs.getInt("id");
				}
			}
			throw new NotDatabaseUpdateException("Объект coordinates не был добавлен");
		} catch (PSQLException e) {
			System.out.println(e.getMessage());
			throw new NotDatabaseUpdateException("Объект coordinates не был добавлен");
		}
	}

	public int selectUserID(String login, String password) throws SQLException, UserNotFoundException {
		PreparedStatement preparedStatement =
						connection.prepareStatement("select * from users where login=? and password=?");
		preparedStatement.setString(1, login);
		preparedStatement.setString(2, password);
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()) {
				return rs.getInt("id");
			}
		}

		throw new UserNotFoundException("Пользователь с таким логином или паролем не найден");
	}

	public Person selectPerson(int id) throws SQLException, PersonNotFoundException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from persons where id=?");
		preparedStatement.setInt(1, id);
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()) {
				String name = rs.getString("name");
				int coords_id = rs.getInt("coordinates_id");
				LocalDateTime creationDate = rs.getTimestamp("creationdate").toLocalDateTime();
				double height = rs.getDouble("height");
				String passport = rs.getString("passport");
				String hairColor = rs.getString("haircolor");
				String nationality = rs.getString("nationality");
				int location_id = rs.getInt("location_id");
				int user_id = rs.getInt("user_id");

				try {
					Coordinates coordinates = selectCoordinates(coords_id);
					Location location = selectLocation(location_id);
					User user = selectUser(user_id);
					return new Person(id, name, creationDate, location, coordinates, height, passport, hairColor, nationality, user);
				} catch (LocationNotFoundException | CoordinatesNotFoundException | UserNotFoundException e) {
					throw new PersonNotFoundException("Ошибка считывания подполей класса person");
				}
			}
		}

		throw new PersonNotFoundException("Нет person с id = " + id);
	}

	public Location selectLocation(int id) throws SQLException, LocationNotFoundException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from locations where id=?");
		preparedStatement.setInt(1, id);
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()) {
				long x = rs.getLong("x");
				int y = rs.getInt("y");
				String name = rs.getString("name");
				return new Location(x, y, name);
			}
		}

		throw new LocationNotFoundException("Нет локации с id = " + id);
	}

	public User selectUser(int id) throws SQLException, UserNotFoundException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id=?");
		preparedStatement.setInt(1, id);
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()) {
				String login = rs.getString("login");
				String password = rs.getString("password");
				return new User(login, password);
			}
		}

		throw new UserNotFoundException("Нет пользователя с id = " + id);
	}

	public Coordinates selectCoordinates(int id) throws SQLException, CoordinatesNotFoundException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from coordinates where id=?");
		preparedStatement.setInt(1, id);
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			if (rs.next()) {
				double x = rs.getDouble("x");
				float y = rs.getFloat("y");
				return new Coordinates(x, y);
			}
		}

		throw new CoordinatesNotFoundException("Нет пользователя с id = " + id);
	}

	public List<Person> selectAllNotes() throws SQLException, NotDatabaseUpdateException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from persons");
		if (preparedStatement.execute()) {
			ResultSet rs = preparedStatement.getResultSet();
			List<Person> persons = new LinkedList<>();
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				int coords_id = rs.getInt("coordinates_id");
				LocalDateTime creationDate = rs.getTimestamp("creationdate").toLocalDateTime();
				double height = rs.getDouble("height");
				String passport = rs.getString("passport");
				String hairColor = rs.getString("haircolor");
				String nationality = rs.getString("nationality");
				int location_id = rs.getInt("location_id");
				int user_id = rs.getInt("user_id");

				try {
					Coordinates coordinates = selectCoordinates(coords_id);
					Location location = selectLocation(location_id);
					User user = selectUser(user_id);
					Person person = new Person(id, name, creationDate, location, coordinates, height, passport, hairColor, nationality, user);
					persons.add(person);
				} catch (LocationNotFoundException | CoordinatesNotFoundException | UserNotFoundException e) {
					throw new NotDatabaseUpdateException("Ошибка в обновлнии коллекции");
				}
			}
			return persons;
		}
		throw new NotDatabaseUpdateException("Ошибка в обновлнии коллекции");
	}

	public void update(int id, Person person) throws SQLException, PersonNotFoundException, NotDatabaseUpdateException {
		PreparedStatement preparedStatement = connection.prepareStatement("update persons set " +
						"(name, coordinates_id, creationdate, height, passport, haircolor, nationality, location_id) " +
						"= (?, ?, ?, ?, ?, ?, ?, ?) where id=?");
		preparedStatement.setString(1, person.getName());
		preparedStatement.setInt(2, insert(person.getCoordinates()));
		preparedStatement.setTimestamp(3, Timestamp.valueOf(person.getCreationDate()));
		preparedStatement.setDouble(4, person.getHeight());
		preparedStatement.setString(5, person.getPassportID());
		preparedStatement.setString(6, person.getHairColor().toString());
		preparedStatement.setString(7, person.getNationality().toString());
		preparedStatement.setInt(8, insert(person.getLocation()));
		preparedStatement.setInt(9, id);
		preparedStatement.execute();
	}


	public void deleteUserNotes(int userID) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("delete from persons where user_id=?");
		preparedStatement.setInt(1, userID);
		preparedStatement.execute();
	}

	public void deleteNote(int id) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("delete from persons where id=?");
		preparedStatement.setInt(1, id);
		preparedStatement.execute();
	}

//	public void deleteGreater(int id) throws SQLException {
//		PreparedStatement preparedStatement = connection.prepareStatement("delete from persons where id>?");
//		preparedStatement.setInt(1, id);
//		preparedStatement.execute();
//	}
}
