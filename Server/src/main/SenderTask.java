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

public class SenderTask implements Runnable {
	private final static int BID_DATA_CONST=7000;
	private static final Logger logger = LoggerFactory.getLogger(ReceiverTask.class);
	private DatagramSocket socket;
	private Answer answer;
	private InetAddress address;
	private int port;

	public SenderTask(DatagramSocket socket, Answer answer, InetAddress address, int port) {
		this.socket = socket;
		this.answer = answer;
		this.address = address;
		this.port = port;
	}

	@Override
	public void run() {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
			objectOutputStream.writeObject(answer);
			objectOutputStream.flush();

			byte[] bytes = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.flush();

			DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, address, port);
			try {
				if (bytes.length>(BID_DATA_CONST*2)) sendBigDataAnswer(bytes, address, port, answer);
				else {
					socket.send(datagramPacket);
					logger.info("Отправлен ответ на " + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
					answer.logAnswer();
				}
			} catch (SocketException | IllegalStateException e) {
				sendBigDataAnswer(bytes, address, port, answer);
			}
		} catch (IOException e) {
			logger.error("Не получилось отправить ответ");
			e.printStackTrace();
		}
	}

	private void sendBigDataAnswer(byte[] bytes, InetAddress address, int port, Answer answer) throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

		logger.info("Слишком большой объем переданных данных");
		Answer bigDataAnswer = new BigDataAnswer();
		partSend(bigDataAnswer,address,port,objectOutputStream,byteArrayOutputStream);

		objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		logger.info("Количество байтов: "+answer.getAnswer().getBytes().length);
		int count = (answer.getAnswer().getBytes().length / BID_DATA_CONST + 1);
		logger.info("Количество переданных пакетов составляет "+count);
		Answer countAnswer = new OkAnswer(Integer.toString(count));
		partSend(countAnswer,address,port,objectOutputStream,byteArrayOutputStream);

		for (int i=0; i<count; i++){
			Answer newAnswer;
			if (i == count-1){
				logger.info("Количество байтов: "+answer.getAnswer().substring(i*BID_DATA_CONST).getBytes().length);
				newAnswer = new OkAnswer(answer.getAnswer().substring(i*BID_DATA_CONST));
			} else {
				logger.info("Количество байтов: "+answer.getAnswer().substring(i*BID_DATA_CONST, (i+1)*BID_DATA_CONST).getBytes().length);
				newAnswer = new OkAnswer(answer.getAnswer().substring(i*BID_DATA_CONST, (i+1)*BID_DATA_CONST));
			}
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			partSend(newAnswer,address,port,objectOutputStream,byteArrayOutputStream);

		}

	}

	private void partSend(Answer answer, InetAddress address, int port, ObjectOutputStream objectOutputStream, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
		objectOutputStream.writeObject(answer);
		objectOutputStream.flush();
		byte[] newBytes = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.flush();
		DatagramPacket datagramPacket = new DatagramPacket(newBytes, newBytes.length, address, port);
		socket.send(datagramPacket);
		logger.info("Отправлен ответ на " + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
		answer.logAnswer();
		objectOutputStream.reset();
		byteArrayOutputStream.reset();
	}
}
