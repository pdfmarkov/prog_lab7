package com.markovpetr.command.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public enum Color implements Serializable {
	GREEN, RED, YELLOW, WHITE;

	/**
	 * Заполняет объект данными полученные из консоли
	 *
	 * @param scanner
	 * @return Color
	 */
	static Color fillColor(Scanner scanner) {
		while (true) {
			System.out.println("Введите одно из значений: green, red, yellow, white");
			String color = scanner.nextLine().trim();
			switch (color) {
				case "green":
					return GREEN;
				case "red":
					return RED;
				case "yellow":
					return YELLOW;
				case "white":
					return WHITE;
				default:
					System.err.println("Вы ввели несуществующее значение.");
			}
		}
	}

	/**
	 * Заполняет объект данными, полученные из файла
	 *
	 * @param scanner
	 * @return Color
	 * @throws IOException В случае ошибки в файле
	 */
	static Color fillColorFromFile(Scanner scanner) throws IOException {
		String color = scanner.nextLine().trim();
		switch (color) {
			case "green":
				return GREEN;
			case "red":
				return RED;
			case "yellow":
				return YELLOW;
			case "white":
				return WHITE;
			default:
				throw new IOException("Вы ввели неверные данные.");
		}
	}

}
