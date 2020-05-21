package com.markovpetr.command.entity.exceptions;

import java.io.IOException;

public class WrongPersonException extends IOException {
    public WrongPersonException(String line){
        System.err.println("Данные внутри "+line+" в Person не соответствуют требованиям! Исправьте, пожалуйста, данное поле");
    }
}
