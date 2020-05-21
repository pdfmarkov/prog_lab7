package com.markovpetr.command.entity.exceptions;

public class SameIdException extends Exception{
    {
        System.err.println("У элементов коллекции в файле совпадают id");
        System.exit(0);
    }
}
