package com.markovpetr.command.commands;


import com.markovpetr.command.commands.exceptions.NotFoundCommandException;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public abstract class Command implements Serializable {
	private static final long serialVersionUID = -6185479884133132648L;
	protected static ArrayList<Command> commands = new ArrayList<>();
	protected Class<?>[] argsTypes;
	protected Persons persons;
	private String name;
	private String description;


	public Command(String name, String description, Class<?>[] argsTypes) {
		this.name = name;
		this.description = description;
		this.argsTypes = argsTypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSignature() {
		return name + " " + Arrays.toString(Arrays.stream(argsTypes)
						.map((arg) -> arg.getName().split("\\."))
						.map((arg) -> arg[arg.length - 1]).toArray())
						.replace('[', '{')
						.replace(']', '}');
	}

	public void validate(Object... args) throws IllegalArgumentException {
		if (args.length == argsTypes.length) {
			for (int i = 0; i < args.length; i++) {
//				argsTypes[i].cast(args[i]);
				if (args[i].getClass() != argsTypes[i]) {
					throw new IllegalArgumentException("Указан неверный тип параметра - " + getSignature());
				}
			}
		} else {
			throw new IllegalArgumentException("Количество параметров не соответсвуют количество параметров команде - " + getSignature());
		}
	}

	@Override
	public String toString() {
		return getSignature() + " - " + description;
	}

	public abstract String execute(User user, Database db, Persons persons, Object... args);

//	public abstract String execute(User user, Persons persons, Object... args);

//	/**
//	 * Считвыает имя команды из строки
//	 *
//	 * @param command строка, которая содержит имя команды
//	 * @return имя команды
//	 */
//	public static String parseName(String command) {
//		return command.split(" ")[0];
//	}

//	/**
//	 * Считывает параметры команды из строки
//	 *
//	 * @param args строка, которая содержит параметры команды
//	 * @return параметры команды
//	 */
//	public static String[] parseArgs(String args) {
//		String[] splitted = args.split(" ", 2);
//		return splitted.length > 1 ? splitted[1].split(" ") : new String[]{};
//	}

//	/**
//	 * Возвращает команду по ее имени
//	 *
//	 * @param name имя команды
//	 * @return Command
//	 * @throws NotFoundCommandException В случае если команда будет не найдена
//	 */
//	public static Command getCommand(String name) throws NotFoundCommandException {
//		for (Command cmd : commands) {
//			if (cmd.getName().equals(name)) {
//				return cmd;
//			}
//		}
//		throw new NotFoundCommandException("Команда '" + name + "' не была инициализирована.");
//	}

//	/**
//	 * @return имя команды
//	 */
//	public String getName() {
//		return signature.split(" ")[0];
//	}

//	public static void initCommand(Persons persons, String signature, String description, Class<? extends Command> clazz)
//					throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//		Command command = clazz.getConstructor(Persons.class).newInstance(persons);
//		command.signature = signature;
//		command.description = description;
//		commands.add(command);
//	}
}
