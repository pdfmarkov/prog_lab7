package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.commands.exceptions.PersonNotFoundException;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;
import com.markovpetr.command.entity.exceptions.NotFound;

import java.sql.SQLException;


public class RemoveByIdCommand extends Command {
	public RemoveByIdCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		validate(args);
		if (user != null) {
			int id = Integer.parseInt((String) args[0]);
			try {
				Person person = db.selectPerson(id);
				if (user.getLogin().equals(person.getOwner().getLogin())) {
					db.deleteNote(id);
					CommandManager.updateCollection();
					return "Запись с индетификатором " + id + " была удалена";
				}
				return "У вас нет прав на удаление этого объекта";
			} catch (PersonNotFoundException e) {
				return "Запись с индетификатором " + id + " не найдена";
			} catch (SQLException e) {
				return e.getMessage();
			}
		}

		return "Вы не вошли в систему";
	}
}
