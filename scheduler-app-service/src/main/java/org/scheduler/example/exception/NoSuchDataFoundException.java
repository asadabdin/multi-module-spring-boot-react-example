package org.scheduler.example.exception;

public class NoSuchDataFoundException extends RuntimeException {

    public NoSuchDataFoundException(String msg) {
        super(msg);
    }
}
