package com.example.converter.exceptions;



public class BadRomanNumberException extends RuntimeException {
    public BadRomanNumberException(String message) {
        super(message);
    }
}
