package main;
//
//import main.commands.Command;
//import main.commands.specific.*;
//import main.entity.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

	private String path;
	{
		String path = System.getenv("LAB_INPUT_PATH");
		if (path == null) {
			this.path = "src\\main\\resources\\newData.json";
		} else if (path.equals("")) {
			System.err.println("Путь в переменной окружения указан не верно/ не указан вообще. Пожалуйста, исправьте путь");
			System.exit(0);
		} else if (!path.endsWith(".json")) {
			System.err.println("Файл должен быть формата json. Пожалуйста, пропишите путь к json файлу");
			System.exit(0);
		} else {
			this.path = path;
		}
	}


//	public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
//			InstantiationException, IllegalAccessException, RightException, SameIdException, FileNotFoundException {
//        Main main = new Main();
//        main.launch();
//	}

//	public void launch() throws InstantiationException, IllegalAccessException, NoSuchMethodException,
//			InvocationTargetException, RightException, SameIdException, FileNotFoundException {
//		Persons persons = new Persons(new File(path));
//		persons.parse();
//
//
//		Command.initCommand(persons,"help", "Выводит справку по коммандам", HelpCommand.class);
//		Command.initCommand(persons,"add {element}", "Добавляет элемент в коллекцию", AddCommand.class);
//		Command.initCommand(persons,"info", "Выводит информацию о коллекции", InfoCommand.class);
//		Command.initCommand(persons,"show", "Выводит все элементы коллекции", ShowCommand.class);
//		Command.initCommand(persons,"update id {element}", "Обновляет значение элемента id", UpdateCommand.class);
//		Command.initCommand(persons,"remove_by_id id", "Удаляет элемент с заданным id", RemoveCommand.class);
//		Command.initCommand(persons,"clear", "Очищает коллекцию", ClearCommand.class);
//		Command.initCommand(persons,"save", "Сохраняет информацию в файл", SaveCommand.class);
//		Command.initCommand(persons,"exit", "Выход, без сохранения", ExitCommand.class);
//		Command.initCommand(persons,"remove_head", "Выводит и удаляет первый элемент коллекции", RemoveHeadCommand.class);
//		Command.initCommand(persons,"add_if_min {element}", "Добавляет элемент в коллекцию, если его " +
//						"значение меньше, чем у минимального в коллекции", AddIfMinCommand.class);
//		Command.initCommand(persons,"remove_greater {element}", "Удаляет из коллекции все элементы, " +
//						"превыщающий заданный", RemoveGreaterCommand.class);
//		Command.initCommand(persons,"min_by_id", "Выводит элемент коллекции, индетификатор которого минимален", MinByIdCommand.class);
//		Command.initCommand(persons,"max_by_coordinates", "Выводи элемент коллекции, у которого значение " +
//						"поля coordinates является максимальным", MaxByCoordinatesCommand.class);
//		Command.initCommand(persons,"count_greater_than_location {location}", "Выводит количество " +
//						"элементов, значение поля location которых больше заданного", CountGreaterThanLocationCommand.class);
//		Command.initCommand(persons,"execute_script file_name", "Считывает и испольняет скрипт из файла", ExecuteScriptCommand.class);
//
//		Scanner scanner = new Scanner(System.in);
//
//		while (true) {
//			System.out.print("Введите команду: ");
//			String line ="";
//			try {
//				line = scanner.nextLine().trim();
//			} catch (NoSuchElementException e){
//				System.err.println("Ты 'exit' напиши, для кого команду прописывали!");
//				System.exit(0);
//			}
//
//			if (!line.equals("")) {
//				try {
//					Command cmd = Command.getCommand(Command.parseName(line));
//					String[] arg = Command.parseArgs(line);
//					cmd.execute(arg);
//				} catch (NoSuchFieldException e) {
//					//System.err.println("Такого поля не существует");
//					System.err.println(e.getMessage());
//				} catch (ArrayIndexOutOfBoundsException e){
//					System.err.println("");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
