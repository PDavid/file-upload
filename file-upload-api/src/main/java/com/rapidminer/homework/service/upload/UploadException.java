package com.rapidminer.homework.service.upload;

public class UploadException extends RuntimeException {

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Exception e) {
        super(message, e);
    }
}
