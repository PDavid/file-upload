package com.rapidminer.homework.service.converter;

public class ConverterException extends RuntimeException {

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Exception e) {
        super(message, e);
    }
}
