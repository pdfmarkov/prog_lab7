package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.commands.exceptions.NotDatabaseUpdateException;
import com.markovpetr.command.commands.exceptions.UserNotFoundException;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;
import java.util.Scanner;


public class AddIfMinCommand extends Command implements Fillable {
	public AddIfMinCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);

			if (user != null) {
				int userID = db.selectUserID(user.getLogin(), user.getPassword());

				if (persons.isEmpty()) {
					return "Элемент не может быть добавлен, т.к. коллекция пуста. " +
									"Добавьте элементы в коллекцию с помощью обычной команды add";
				} else {
					Person person = (Person) args[0];

					long min = Long.MAX_VALUE;
					for (Person p : persons) {
						if (p.getId() < min) {
							min = p.getId();
						}
					}

					if (min > person.getId()) {
						db.insert(person, userID);
						CommandManager.updateCollection();
						return "Элемент добавлен, так как его значение " + person.getId() + " меньше чем минимальное " + min;
					} else {
						return "Элемент не добавлен, так как его значение " + person.getId() + " больше или равно минимальному " + min;
					}
				}
			}

			return "Вы не вошли в систему";
		} catch (IllegalArgumentException | SQLException | NotDatabaseUpdateException | UserNotFoundException e) {
			return e.getMessage();
		}
	}


	@Override
	public Object[] fill(Scanner scanner) {
		Object[] args = new Object[1];
		args[0] = Person.fillPerson(scanner);
		return args;
	}
}

