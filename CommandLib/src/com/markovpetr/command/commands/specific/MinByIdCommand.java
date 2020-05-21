package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.util.Comparator;


public class MinByIdCommand extends Command {
	public MinByIdCommand(String name, String description, Class<?>[] argsTypes) {
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
					return persons.stream().min(Comparator.comparing(Person::getId)).get().toString();
				}
			}

			return "Вы не вошли в систему";
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

//		if (args.length != 0) {
//			System.err.println("В команде " + getName() + " не должно быть параметров");
//		} else if (persons.isEmpty()) {
//			System.err.println("Команда не может быть выполнена, т.к. коллекция пуста. Добавьте элементы в коллекцию с помощью команды add");
//		}else {
//			Person min = persons.peek();
//			for (Person person : persons) {
//				if (min.getId() > person.getId()) {
//					min = person;
//				}
//			}
//
//			System.out.println(min.toString());
//		}
	}
}
