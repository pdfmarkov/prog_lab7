package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.commands.exceptions.PersonNotFoundException;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;


public class RemoveGreaterCommand extends Command implements Fillable {
	public RemoveGreaterCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		validate(args);
		if (user != null) {
			try {
				if (persons.isEmpty()) {
					return "Команда не может быть выполнена, т.к. коллекция пуста. " +
									"Добавьте элементы в коллекцию с помощью команды add";
				} else {
					Person person = (Person) args[0];

					Iterator<Person> iter = persons.iterator();
					while (iter.hasNext()) {
						Person p = iter.next();
						if (p.hashCode() > person.hashCode()) {
							if (user.getLogin().equals(p.getOwner().getLogin())) {
								db.deleteNote(Math.toIntExact(p.getId()));
							}
						}
					}

					CommandManager.updateCollection();
					return "Были удалены все элементы со значением выше " + person.hashCode();
				}
			} catch (SQLException e) {
				return e.getMessage();
			}
		}
		return "Вы не вошли в систему";
	}

	@Override
	public Object[] fill(Scanner scanner) {
		Object[] args = new Object[1];
		args[0] = Person.fillPerson(scanner);
		return args;
	}
}
