package com.markovpetr.command.commands.specific;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.Database;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.Persons;
import com.markovpetr.command.entity.User;

import java.sql.SQLException;


public class RemoveHeadCommand extends Command {
	public RemoveHeadCommand(String name, String description, Class<?>[] argsTypes) {
		super(name, description, argsTypes);
	}

	@Override
	public String execute(User user, Database db, Persons persons, Object... args) {
		validate(args);

		if (user != null) {
			if (persons.isEmpty()) {
				return "Команда не может быть выполнена, т.к. коллекция пуста. " +
								"Добавьте элементы в коллекцию с помощью команды add";
			} else {
				try {
					Person person = persons.peek();
					if (user.getLogin().equals(person.getOwner().getLogin())) {
						db.deleteNote(Math.toIntExact(person.getId()));
						CommandManager.updateCollection();
						return "Объект удален: " + person;
					}
					return "У вас нет прав на удаление этого объекта";
				} catch (SQLException e) {
					return e.getMessage();
				}
			}
		}

		return "Вы не вошли в систему";
	}
}
