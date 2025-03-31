package com.example.project.exception;

public class ErrorMessages {
    private ErrorMessages() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }
    public static final String CAR_NOT_FOUND = "Car with id %d not found";
    public static final String USER_NOT_FOUND = "User with id %d not found";
    public static final String DEALERSHIP_NOT_FOUND = "Dealership with id %d not found";
}
