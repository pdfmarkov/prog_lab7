package com.markovpetr.command.entity;


import com.markovpetr.command.reader.ConsoleReader;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

public class Coordinates implements Serializable {
	private static final long serialVersionUID = -599087130950208425L;
	private Double x;
	private float y;

	public Coordinates(Double x, float y) {
		if (x == null) {
			throw new NullPointerException("Параметр 'x' не может быть равен null");
		}
		this.x = x;
		this.y = y;
	}

	/**
	 * Заполняет объект данными полученные из консоли
	 *
	 * @param scanner
	 * @return Coordinates
	 */
	public static Coordinates fillCoordinates(Scanner scanner) {
		System.out.println("Ввод объека Coordinates:");
		Double x = (Double) ConsoleReader.conditionalRead(scanner, "Введите x: ",false,
						Double::parseDouble, (m) -> Double.parseDouble(m) > 0);
		float y = (float) ConsoleReader.conditionalRead(scanner, "Введите y: ",false,
						Float::parseFloat, Objects::nonNull, (m) -> Float.parseFloat(m) - 0.0001f > 0);
		return new Coordinates(x, y);
	}

	/**
	 * Заполняет объект данными полученные из консоли
	 *
	 * @param scanner
	 * @return Coordinates
	 * @throws IOException В случае ошибки в файле
	 */
	public static Coordinates fillCoordinatesFromFile(Scanner scanner) throws IOException {
		Double x = (Double) ConsoleReader.conditionalRead(scanner,"",false,
				Double::parseDouble, (m) -> Double.parseDouble(m) > 0);
		float y = (float) ConsoleReader.conditionalRead(scanner,"",false,
				Float::parseFloat, Objects::nonNull, (m) -> Float.parseFloat(m) > 0);
		return new Coordinates(x, y);
	}

	public Double getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	/**
	 * Модуль координат x и y
	 *
	 * @return double
	 */
	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	@Override
	public String toString() {
		return "Coordinates{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
