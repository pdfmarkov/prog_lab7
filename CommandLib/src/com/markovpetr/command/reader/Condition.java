package com.markovpetr.command.reader;

@FunctionalInterface
public interface Condition<T> {
	boolean check(String x);
}
