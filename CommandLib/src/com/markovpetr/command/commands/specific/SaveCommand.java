package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;


public class SaveCommand extends Command {
	public SaveCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
//			persons.save();
			return "Коллекция сохранена";
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

//		if (args.length != 0) {
//			System.err.println("В команде " + getName() + " не должно быть параметров");
//		} else {
//			persons.save();
//		}
	}
}
