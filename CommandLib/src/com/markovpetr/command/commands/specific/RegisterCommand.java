package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.commands.exceptions.NotDatabaseUpdateException;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;
import java.util.Scanner;

public class RegisterCommand extends Command implements Fillable {
	public RegisterCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
			User regUser = (User) args[0];
			db.insert(regUser);
			return "Пользователь " + regUser.getLogin() + " успешно зарегистрирован";
		} catch (SQLException | NotDatabaseUpdateException e) {
			return e.getMessage();
		}
	}

	@Override
	public Object[] fill(Scanner scanner) {
		Object[] args = new Object[1];
		args[0] = User.fillUser(scanner);
		return args;
	}
}
