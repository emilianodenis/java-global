package com.example.demo.model.exception;

public class ProfessionNotFoundException extends RuntimeException {
    public ProfessionNotFoundException(Integer id) {
        super(String.format("Could not find profession %s", id));
    }
}
