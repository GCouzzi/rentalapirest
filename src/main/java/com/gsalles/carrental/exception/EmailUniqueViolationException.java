package com.gsalles.carrental.exception;

public class EmailUniqueViolationException extends RuntimeException{
    public EmailUniqueViolationException(String message) {
        super(message);
    }
}
