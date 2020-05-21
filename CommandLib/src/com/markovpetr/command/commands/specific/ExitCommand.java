package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;


public class ExitCommand extends Command {
	public ExitCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
			if (user != null) {
				System.exit(0);
				return "Программа успешно завершена!";
			}
			return "Вы не вошли в систему";
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

//		if (args.length != 0) {
//			System.err.println("В команде " + getName() + " не должно быть параметров");
//		} else {
//			System.out.println("Программа успешно завершена!");
//			System.exit(1);
//		}
	}
}
