package com.example.project.exception;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidError extends RuntimeException {
    private HttpStatus status;
    private Map<String, String> errors;

    // Конструктор
    public ValidError(HttpStatus status, Map<String, String> errors) {
        super(status.getReasonPhrase());  // Строковое описание ошибки, передаем в родительский конструктор
        this.status = status;
        this.errors = errors;
    }

    // Переопределяем getMessage() для предоставления строки
    @Override
    public String getMessage() {
        // Возвращаем строковое представление карты ошибок
        return "Error: " + errors.toString();
    }

    // Геттеры
    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
