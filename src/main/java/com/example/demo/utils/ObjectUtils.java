package com.example.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class ObjectUtils {

    public static byte[] serialize(Serializable object) throws IOException {
        try (var byteOutputStream = new ByteArrayOutputStream(); var objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
            objectOutputStream.writeObject(object);
            return byteOutputStream.toByteArray();
        }
    }

    public static <T> T deSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (var inputStream = new ByteArrayInputStream(bytes); var outputStream = new ObjectInputStream(inputStream)) {
            return (T) outputStream.readObject();
        }
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
