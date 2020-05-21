package com.markovpetr.command.entity;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Persons extends PriorityQueue<Person> {
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();

	private LocalDateTime initDate;
	private File file;

	public Persons(File file) {
		this.file = file;
		initDate = LocalDateTime.now();
	}

	public Persons() {
		initDate = LocalDateTime.now();
	}

	public void update(List<Person> list) {
		writeLock.lock();
		readLock.lock();
		this.clear();
		this.addAll(list);
		writeLock.unlock();
		readLock.unlock();
	}

	@Override
	public String toString() {
		return  "Тип коллекции: PriorityQueue\n" +
						"Дата инициализации: " + initDate.toString() + "\n" +
						"Количество элементов: " + this.size();
	}
}