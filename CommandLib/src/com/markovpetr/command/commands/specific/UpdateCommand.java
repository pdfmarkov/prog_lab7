package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.commands.exceptions.NotDatabaseUpdateException;
import com.markovpetr.command.commands.exceptions.PersonNotFoundException;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;
import com.markovpetr.command.entity.exceptions.NotFound;

import java.sql.SQLException;
import java.util.Scanner;


public class UpdateCommand extends Command implements Fillable {

	public UpdateCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
			if (user != null) {
				if (persons.isEmpty()) {
					return "Команда не может быть выполнена, т.к. коллекция пуста. " +
									"Добавьте элементы в коллекцию с помощью команды add";
				} else {
					int id = Integer.parseInt((String) args[0]);
					Person person = (Person) args[1];
					try {
						Person dbPerson = db.selectPerson(id);

						if (user.getLogin().equals(dbPerson.getOwner().getLogin())) {
							db.update(id, person);
							return "Объект успешно обновлен";
						}
						return "У вас нет прав на измение этого объекта";
					} catch (SQLException | PersonNotFoundException | NotDatabaseUpdateException e) {
						return e.getMessage();
					}
				}
			}
			return "Вы не вошли в систему";
		} catch (IllegalArgumentException e) {
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
