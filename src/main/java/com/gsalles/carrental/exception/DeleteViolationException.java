package com.gsalles.carrental.exception;

public class DeleteViolationException extends RuntimeException{
    public DeleteViolationException(String message) {
        super(message);
    }
}
