package com.markovpetr.command.commands.exceptions;

public class NotDMLorDDLCommandException extends Exception {
	public NotDMLorDDLCommandException(String message) {
		super(message);
	}
}
