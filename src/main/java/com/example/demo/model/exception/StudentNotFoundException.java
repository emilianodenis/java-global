package com.example.demo.model.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Integer id) {
        super(String.format("Could not find student %s", id));
    }
}
