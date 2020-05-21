package main;

import main.answers.Answer;
import main.answers.BigDataAnswer;
import main.answers.OkAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ForkJoinPool;

public class Sender extends Thread {
	private ForkJoinPool pool;
	private DatagramSocket socket;

	public Sender(DatagramSocket socket, int nThreads) {
		pool = new ForkJoinPool(nThreads);
		this.socket = socket;
	}

	@Override
	public void run() {
		while (!isInterrupted());
	}

	public void send(Answer answer, InetAddress address, int port) {
		SenderTask task = new SenderTask(socket, answer, address, port);
		pool.execute(task);
	}
}
