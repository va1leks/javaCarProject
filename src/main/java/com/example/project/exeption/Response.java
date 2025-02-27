package com.example.project.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
