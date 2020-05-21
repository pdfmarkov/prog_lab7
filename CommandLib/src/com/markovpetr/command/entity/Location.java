package com.markovpetr.command.entity;

import com.google.gson.Gson;
import com.markovpetr.command.commands.Fillable;
import com.markovpetr.command.reader.ConsoleReader;


import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

public class Location implements Serializable {
	private static final long serialVersionUID = 2015758969639819042L;
	private Long x;
	private Integer y;
	private String name;

	public Location() {}

	public Location(Long x, Integer y, String name) {
		if (x == null) {
			throw new NullPointerException("Параметр 'x' не может быть равен null");
		}
		this.x = x;

		if (y == null) {
			throw new NullPointerException("Параметр 'y' не может быть равен null");
		}
		this.y = y;

		if(name == null || name.equals("")) this.name = null;
		else this.name = name;
	}

	/**
	 * Модуль координат x и y
	 *
	 * @return double
	 */
	public double length() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

//	/**
//	 * Переводит объект в json строку
//	 *
//	 * @return json String
//	 */
//	public String toJSON() {
//		return new Gson().toJson(this);
//	}

//	/**
//	 * Считывает объект из json строки
//	 *
//	 * @param json объект в строке
//	 * @return Location
//	 */
//	public static Location parseLocation(String json) {
//		return new Gson().fromJson(json, Location.class);
//	}

	/**
	 * Заполняет объект по данным полученным из консоли
	 *
	 * @param scanner
	 * @return Location
	 */
	public static Location fillLocation(Scanner scanner) {
		System.out.println("Ввод объека Location:");
		Long x = (Long) ConsoleReader.conditionalRead(scanner, "Введите x: ",false,
						Long::parseLong, (m) -> Double.parseDouble(m) > 0);
		Integer y = (Integer) ConsoleReader.conditionalRead(scanner, "Введите y: ",false,
						Integer::parseInt, Objects::nonNull);
		String name = (String) ConsoleReader.conditionalRead(scanner, "Введите имя: ",true,
						String::toString, m -> !m.equals(""));
		return new Location(x, y, name);
	}

	/**
	 * Заполняет объект по данным полученным из файла
	 *
	 * @param scanner
	 * @return Location
	 * @throws IOException В случае ошибок в файле
	 */
	public static Location fillLocationFromFile(Scanner scanner) throws IOException {
		Long x = (Long) ConsoleReader.conditionalRead(scanner,"", false,
				Long::parseLong, (m) -> Double.parseDouble(m) > 0);
		Integer y = (Integer) ConsoleReader.conditionalRead(scanner,"",false,
				Integer::parseInt, Objects::nonNull);
		String name = (String) ConsoleReader.conditionalRead(scanner,"",true,
				String::toString, m -> !m.equals(""));
		return new Location(x, y, name);
	}

	public Long getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Location{" +
				"x=" + x +
				", y=" + y +
				", name='" + name + '\'' +
				'}';
	}
}
