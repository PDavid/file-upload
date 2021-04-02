package com.rapidminer.homework.service.storage;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Exception e) {
        super(message, e);
    }
}
