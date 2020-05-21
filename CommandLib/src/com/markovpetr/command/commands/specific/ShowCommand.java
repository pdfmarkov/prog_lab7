package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.util.Arrays;


public class ShowCommand extends Command {
	public ShowCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);

			if (user != null) {
				if (persons.isEmpty()) {
					return "Коллекция пустая. Данные внутри файла отсутствуют!";
				} else {
					Object[] out = persons.toArray();
					Arrays.sort(out);
					StringBuilder builder = new StringBuilder();
					for (Object o : out) {
						builder.append(o).append("\n");
					}
					return builder.toString();
				}
			}

			return "Вы не вошли в систему";
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

//		if (args.length != 0) {
//			System.err.println("В команде " + getName() + " не должно быть параметров");
//		} else if(persons.isEmpty()){
//			System.out.println("Коллекция пустая. Данные внутри файла отсутствуют!");
//		}
//		else {
////			Способ через Stream API
////			Arrays.stream(persons.toArray()).sorted().forEach(System.out::println);
//
//			Object[] out = persons.toArray();
//			Arrays.sort(out);
//			for (Object o : out) System.out.println(o);
//		}
	}
}
