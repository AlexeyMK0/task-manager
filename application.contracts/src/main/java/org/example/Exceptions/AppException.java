package org.example.Exceptions;

public abstract class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
