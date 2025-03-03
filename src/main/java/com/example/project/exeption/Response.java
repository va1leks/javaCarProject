package com.example.project.exeption;


import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Data
public class Response {

    private String message;
    private HttpStatus httpStatus;

    public Response(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }

}
