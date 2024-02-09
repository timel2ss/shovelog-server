package me.shovelog.exception;

public abstract class ShovelogException extends RuntimeException {
    public ShovelogException(String message) {
        super(message);
    }

    public ShovelogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
