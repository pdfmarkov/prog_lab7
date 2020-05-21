package com.markovpetr.command.reader;

import java.util.Scanner;

public class ConsoleReader {
	/**
	 * Вводит значение до тех пор, пока оно не будет удовлетворять указанным условиям
	 *
	 * @param scanner Сканнер
	 * @param phrase Фраза, которая будет показана перед вводом
	 * @param caster Лямда-выражение, которое производит преобразование в нужный тип данных
	 * @param conditions Лямда-выражения, условия для проверки
	 */
	public static Object conditionalRead(Scanner scanner, String phrase, boolean canNull, Caster caster, Condition... conditions) {
		boolean checkedConditions;
		String next;
		while (true) {
			checkedConditions = true;
			System.out.print(phrase);
			next = scanner.nextLine().trim();
			if (canNull && next.equals("")) {
				return null;
			} else for (Condition cond : conditions) {
				try {
					if (next.equals("")) {
						checkedConditions = false;
						if (!phrase.equals("")) System.err.println("Вы ввели данные не верно.");
						break;
					}
					if (!cond.check(next)) {
						checkedConditions = false;
						if (!phrase.equals("")) System.err.println("Вы ввели данные не верно.");
						break;
					}
				} catch (Exception e) {
					if (!phrase.equals("")) System.err.println("Вы ввели данные не верно.");
					checkedConditions = false;
				}

				if (checkedConditions) {
					return caster.cast(next);
				}
			}
		}
	}
}
