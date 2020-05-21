package main;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Receiver extends Thread {
	private ThreadPoolExecutor executor;
	private DatagramSocket socket;
	private Interpreter interpreter;
	private Sender sender;

	public Receiver(DatagramSocket socket, Interpreter interpreter, Sender sender, int nThreads) {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
		this.socket = socket;
		this.interpreter = interpreter;
		this.sender = sender;
	}

	@Override
	public void run() {
		for (int i = 0; i < executor.getMaximumPoolSize(); i++) {
			ReceiverTask task = new ReceiverTask(socket, interpreter, sender);
			executor.execute(task);
		}
	}
}
