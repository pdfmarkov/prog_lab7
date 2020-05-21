package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.commands.exceptions.NotDatabaseUpdateException;
import com.markovpetr.command.commands.exceptions.UserNotFoundException;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;
import java.util.Scanner;


public class AddCommand extends Command implements Fillable {

	public AddCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons person, Object... args) {
		try {
			validate(args);
			if (user != null) {
				int userID = db.selectUserID(user.getLogin(), user.getPassword());
				db.insert(((Person) args[0]), userID);
				CommandManager.updateCollection();
				return "Элемент добавлен в коллекцию";
			}

			return "Вы не вошли в систему";
		} catch (IllegalArgumentException | SQLException | NotDatabaseUpdateException | UserNotFoundException e) {
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
