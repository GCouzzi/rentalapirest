package com.gsalles.carrental.exception;

import java.io.Serial;

public class AutomovelUniqueViolationException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public AutomovelUniqueViolationException(String message) {
        super(message);
    }
}
