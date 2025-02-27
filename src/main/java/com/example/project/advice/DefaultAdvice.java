package com.example.project.advice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.project.exeption.CarException;
import com.example.project.exeption.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(CarException.class)
    public ResponseEntity<Response> handleException(CarException e) {
        Response response = new Response(NOT_FOUND + e.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }
}
