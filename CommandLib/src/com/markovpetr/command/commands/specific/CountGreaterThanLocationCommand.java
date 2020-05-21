package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.entity.Location;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.util.Scanner;


public class CountGreaterThanLocationCommand extends Command implements Fillable {
	public CountGreaterThanLocationCommand(String name, String description, Class<?>[] argsTypes) {
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
					Location location = (Location) args[0];
					return "Количество элементов большее заданного: " +
									persons.stream().filter(person -> person.getLocation().length() > location.length()).count();
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
		args[0] = Location.fillLocation(scanner);
		return args;
	}
}
