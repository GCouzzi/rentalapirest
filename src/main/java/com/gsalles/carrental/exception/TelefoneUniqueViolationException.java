package com.gsalles.carrental.exception;

public class TelefoneUniqueViolationException extends RuntimeException{
    public TelefoneUniqueViolationException(String message) {
        super(message);
    }
}
