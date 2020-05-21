package com.markovpetr.command.entity.exceptions;

public class RightException extends Exception{
    public RightException(String arg){
        System.err.println(arg);
        System.exit(0);
    }
}