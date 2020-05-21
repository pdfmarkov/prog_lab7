package com.markovpetr.command.reader;

@FunctionalInterface
public interface Caster<T> {
	T cast(String x);
}
