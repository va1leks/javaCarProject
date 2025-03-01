package com.example.project.exeption;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Object> handleException(CarNotFoundException e) {
        Response response = new Response(NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

}
