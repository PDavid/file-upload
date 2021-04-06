package com.rapidminer.fileupload.service.converter;

public class ConversionException extends RuntimeException {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Exception e) {
        super(message, e);
    }
}
