package com.markovpetr.command.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.entity.exceptions.WrongDateTimeException;
import com.markovpetr.command.entity.exceptions.WrongPersonException;
import com.markovpetr.command.reader.ConsoleReader;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Person implements Comparable<Person>, Serializable {
	private static final long serialVersionUID = -6565259818539791441L;
	private static Gson gson = new GsonBuilder()/*.serializeNulls()*/.create();

	private long id;
	private LocalDateTime creationDate;

	private Location location;
	private String name;
	private Coordinates coordinates;
	private double height;
	private String passportID;
	private Color hairColor;
	private Country nationality;
	private User owner;

	public Person() { }

	public Person(long id, String name, LocalDateTime creationDate, Location location, Coordinates coords, double height,
	              String passportID, String hairColor, String nationality, User owner) {
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.location = location;
		this.coordinates = coords;
		this.height = height;
		this.passportID = passportID;
		this.hairColor = Color.valueOf(hairColor);
		this.nationality = Country.valueOf(nationality);
		this.owner = owner;
	}

//	/**
//	 * Считвыает Person из json строки.
//	 *
//	 * @param json Объект в json формате
//	 * @return Person
//	 */
//	public static Person parsePerson(String json) throws WrongPersonException {
//		Person person = null;
//		try {
//			String finding = ",\"y\":";
////			System.out.println(finding);
////			System.out.println(json);
//			person = gson.fromJson(json, Person.class);
//			person.validate();
//			int occurrencesCount = json.length() - json.replace(finding, "").length();
//			if ((json.contains((char) 34+"y"+(char) 34+":null")) || occurrencesCount !=10)  throw new WrongPersonException("coordinates(Y не может быть null/Y должен существовать)");
//		} catch (WrongPersonException e){
//			e.getMessage();
//			System.exit(1);
//		}
//		return person;
//	}

//	/**
//	 * Переводит объект в json строку
//	 *
//	 * @return String json
//	 */
//	public String toJSON() {
//		return gson.toJson(this);
//	}

//	public void setId(Integer id) {
//		this.id = id;
//	}

	/**
	 * Проверка JSON файла на соотвествие параметров Person всем условиям
	 *
	 * @throws WrongPersonException если Person создан некорректно
	 * @throws WrongDateTimeException если дата/время не являются корректными
	 */
	public void validate() throws WrongPersonException {
		try {
			if (this.id <= 0) throw new WrongPersonException("id");
			if (this.name == null || this.name.equals("")) throw new WrongPersonException("name");
			if (this.coordinates == null) throw new WrongPersonException("coordinates");
			if (this.creationDate == null) throw new WrongPersonException("creationDate");
			if (this.height <= 0) throw new WrongPersonException("height");
			if (this.hairColor == null) throw new WrongPersonException("hairColor");
			if (this.location == null) throw new WrongPersonException("location");
			if (this.coordinates.getX() == null) throw new WrongPersonException("coordinates (X)");
			if ((this.passportID != null && this.passportID.length() < 4)) throw new WrongPersonException("passportID");
			else if (!this.getCreationDate().equals(creationDate)) throw new WrongDateTimeException();
		} catch (WrongPersonException e){
			e.getMessage();
			System.exit(0);
		}
	}


	public LocalDateTime getCreationDate() {
		if (creationDate == null) throw new WrongDateTimeException();
		else {
			try {
				LocalDate.parse(creationDate.toLocalDate().toString());
				LocalTime.parse(creationDate.toLocalTime().toString());
				return creationDate;
			} catch (DateTimeException e) {
				throw new WrongDateTimeException();
			}
		}
	}

	/**
	 * Заполняет Person данными полученными с консоли
	 *
	 * @param scanner
	 * @return Person
	 */
	public static Person fillPerson(Scanner scanner) {
		Person person = new Person();
		try {
			System.out.println("Ввод объекта Person:");
			person.name = (String) ConsoleReader.conditionalRead(scanner, "Введите имя: ",false,
					String::toString, Objects::nonNull, (m) -> !m.equals(""));
			person.coordinates = Coordinates.fillCoordinates(scanner);
			person.creationDate = LocalDateTime.now();
			person.height = (double) ConsoleReader.conditionalRead(scanner, "Введите рост: ",false,
					Double::parseDouble, (m) -> Double.parseDouble(m) > 0);
			person.passportID = (String) ConsoleReader.conditionalRead(scanner, "Введите индетификатор паспорта: ", true,
					String::toString, (m) -> m.length() > 3, (m) -> !m.equals(""));
			person.hairColor = Color.fillColor(scanner);
			person.nationality = Country.fillCountry(scanner);
			person.location = Location.fillLocation(scanner);
//			person.id = IdGenerator.generateUniqueId();
		} catch (NoSuchElementException e) {
			System.err.println("Ну почему-ты до конца не ввел Person :(");
			System.exit(0);
		}
		return person;
	}


//	public static class IdGenerator {
//		public static long generateUniqueId() {
//			UUID idOne = UUID.randomUUID();
//			String str = "" + idOne;
//			int uid = str.hashCode();
//			String filterStr = "" + uid;
//			str = filterStr.replaceAll("-", "");
//			return Long.parseLong(str);
//		}
//	}

	public Long getId() {
	return id;
}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public double getHeight() {
		return height;
	}

	public String getPassportID() {
		return passportID;
	}

	public Color getHairColor() {
		return hairColor;
	}

	public Country getNationality() {
		return nationality;
	}

	public User getOwner() {
		return owner;
	}

	@Override
	public String toString() {
		return hashCode() + ": " + gson.toJson(this);
	}

	@Override
	public int hashCode() {
			int result = 17;
			result = 31 * result + name.hashCode();
			result = 31 * result + (location == null ? 0 : location.hashCode());
			result = 31 * result + (coordinates == null ? 0 : coordinates.hashCode());
			result = 31 * result + (hairColor == null ? 0 : hairColor.hashCode());
			result = 31 * result + (nationality == null ? 0 : nationality.hashCode());
			result = 31 * result + (passportID == null ? 0 : passportID.hashCode());
			return result;
		}

	@Override
	public int compareTo(Person p) {
		return Integer.compare(this.hashCode(), p.hashCode());
	}

	public double getCoordsLength() {
		return coordinates.length();
	}
}
