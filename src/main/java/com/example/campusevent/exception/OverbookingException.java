package com.example.campusevent.exception;

public class OverbookingException extends RuntimeException {
    public OverbookingException(String message) {
        super(message);
    }
}
