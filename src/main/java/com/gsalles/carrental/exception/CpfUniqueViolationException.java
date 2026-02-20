package com.gsalles.carrental.exception;

public class CpfUniqueViolationException extends RuntimeException{
    public CpfUniqueViolationException(String message) {
        super(message);
    }
}
