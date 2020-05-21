package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;


public class HelpCommand extends Command {
	public HelpCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
			return CommandManager.getCommandsInfo();
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}

//		if (args.length != 0) {
//			System.err.println("В команде " + getName() + " не должно быть параметров");
//		} else {
//			StringBuilder builder = new StringBuilder();
//
//			for (Command cmd : commands) {
//				builder.append(cmd.toString()).append("\n");
//			}
//
//			System.out.println(builder.toString().trim());
//		}
	}
}
