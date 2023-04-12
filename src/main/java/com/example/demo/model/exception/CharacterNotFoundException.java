package com.example.demo.model.exception;

public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(Integer id) {
        super(String.format("Could not find character %s", id));
    }
}
