package main;

import com.markovpetr.command.commands.Command;
import com.markovpetr.command.commands.CommandManager;
import com.markovpetr.command.commands.exceptions.CommandAlreadyExistsException;
import com.markovpetr.command.commands.exceptions.NotFoundCommandException;
import com.markovpetr.command.commands.specific.*;
import com.markovpetr.command.entity.Location;
import com.markovpetr.command.entity.Person;
import com.markovpetr.command.entity.User;
import com.markovpetr.command.entity.exceptions.RightException;
import com.markovpetr.command.entity.exceptions.SameIdException;
import main.answers.Answer;
import main.answers.OkAnswer;
import main.answers.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Interpreter {
	private static final Logger logger = LoggerFactory.getLogger(ReceiverTask.class);

	private String path;
	{
		String path = System.getenv("LAB_INPUT_PATH");
		if (path == null) {
			this.path = "newData.json";
		} else if (path.equals("")) {
			logger.error("Путь в переменной окружения указан не верно/ не указан вообще. Пожалуйста, исправьте путь");
			System.exit(0);
		} else if (!path.endsWith(".json")) {
			logger.error("Файл должен быть формата json. Пожалуйста, пропишите путь к json файлу");
			System.exit(0);
		} else {
			this.path = path;
		}
	}

	public Interpreter() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CommandAlreadyExistsException, SameIdException, RightException, ClassNotFoundException {
		CommandManager manager = CommandManager.getInstance("postgres", "Buzuluk2002");
		manager.initCommand(AddCommand.class, "add", "Добавляет элемент в коллекцию", Person.class);
//		manager.initCommand(AddIfMinCommand.class, "add_if_min", "Добавляет элемент в коллекцию, если его " +
//						"значение меньше, чем у минимального в коллекции", Person.class);
		manager.initCommand(ClearCommand.class, "clear", "Очищает коллекцию");
		manager.initCommand(CountGreaterThanLocationCommand.class, "count_greater_than_location",
						"Выводит количество элементов, значение поля location которых больше заданного", Location.class);
		manager.initCommand(ExecuteScriptCommand.class, "execute_script",
						"Считывает и испольняет скрипт из файла", String.class);
		manager.initCommand(ExitCommand.class, "exit", "Выход, с сохранением информацией в файл");
		manager.initCommand(HelpCommand.class, "help", "Выводит справку по коммандам");
		manager.initCommand(InfoCommand.class, "info", "Выводит информацию о коллекции");
		manager.initCommand(MaxByCoordinatesCommand.class, "max_by_coordinates",
						"Выводит элемент коллекции, у которого значение поля coordinates является максимальным");
		manager.initCommand(MinByIdCommand.class, "min_by_id",
						"Выводит элемент коллекции, индетификатор которого минимален");
		manager.initCommand(RemoveByIdCommand.class, "remove_by_id",
						"Удаляет элемент с заданным id", String.class);
		manager.initCommand(RemoveGreaterCommand.class, "remove_greater",
						"Удаляет из коллекции все элементы, превыщающий заданный", Person.class);
		manager.initCommand(RemoveHeadCommand.class, "remove_head", "Выводит и удаляет первый элемент коллекции");
//		manager.initCommand(SaveCommand.class, "save", "Сохраняет информацию в файл");
		manager.initCommand(ShowCommand.class, "show", "Выводит все элементы коллекции");
		manager.initCommand(UpdateCommand.class, "update", "Обновляет значение элемента id",
						String.class, Person.class);
		manager.initCommand(AuthCommand.class, "auth", "Авторизует пользователя", User.class);
		manager.initCommand(RegisterCommand.class, "register", "Региструет пользователя", User.class);

		CommandManager.updateCollection();
	}

	public Answer interpret(Request request) {
		User user = request.getUser();
		Command cmd = request.getCommand();
		Object[] args = request.getArgs();
		return new OkAnswer(CommandManager.execute(user, cmd, args));
	}

	public void askCommand(Scanner scanner) {
		while(true) {
			try {
//				System.out.print(">> ");
				String line = scanner.nextLine().trim();
				String name = CommandManager.parseName(line);

				Object[] args = CommandManager.parseArgs(line);
				Command command = CommandManager.getCommand(name);
				Object[] fillableArg = CommandManager.getFillableArgs(command, scanner);
				args = CommandManager.concatArgs(args, fillableArg);

				CommandManager.validate(command, args);
				logger.info(CommandManager.execute(new User("server", "server"), command, args));
//				System.out.println("Команда успешна провалидирована");
			} catch (NotFoundCommandException | IllegalArgumentException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
