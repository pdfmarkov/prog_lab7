package main;

import com.markovpetr.command.commands.exceptions.CommandAlreadyExistsException;
import com.markovpetr.command.entity.exceptions.RightException;
import com.markovpetr.command.entity.exceptions.SameIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
	public static int SERVER_PORT = 3292;
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private static ThreadPoolExecutor executor;

	public static void main(String[] args) throws SameIdException, InvocationTargetException, IllegalAccessException, InstantiationException, RightException, NoSuchMethodException, CommandAlreadyExistsException, ClassNotFoundException {
		Server server = new Server();
		server.launch();
	}

	public Server() {}

	public void launch() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, CommandAlreadyExistsException, SameIdException, RightException, ClassNotFoundException {
		DatagramSocket socket = setSocket();

		if (socket != null) {
			logger.info("Сервер запущен");

			Scanner scanner = new Scanner(System.in);
			Interpreter interpreter = new Interpreter();
			Sender sender = new Sender(socket, 4);
			Receiver receiver = new Receiver(socket, interpreter, sender, 4);

			receiver.setDaemon(true);
			sender.setDaemon(true);
			receiver.start();
			sender.start();

			shutDownHook();
			while (true) {
				interpreter.askCommand(scanner);
			}
		}
	}

	public DatagramSocket setSocket() {
		try {
			return new DatagramSocket(SERVER_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void shutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Сервер остановлен");
		}));
	}
}
