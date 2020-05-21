package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.exceptions.UserNotFoundException;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;


public class ClearCommand extends Command {
	public ClearCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		try {
			validate(args);
			if (user != null) {
				int userID = db.selectUserID(user.getLogin(), user.getPassword());
				db.deleteUserNotes(userID);
				CommandManager.updateCollection();
				return "Коллекция очищена";
			}
			return "Вы не вошли в систему";
		} catch (IllegalArgumentException | UserNotFoundException | SQLException e) {
			return e.getMessage();
		}
	}
}
