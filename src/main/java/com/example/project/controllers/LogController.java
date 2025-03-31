package com.example.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1/logs")
@Tag(name = "log Controller", description = "API для управления логами")
public class LogController {

    private static final String LOG_DIRECTORY = "logs/";
    // Директория хранения логов
    @Operation(summary = "Получить log файл")
    @GetMapping("/{date}")
    public ResponseEntity<Resource> getLogFile(@PathVariable String date) {
        String fileName = LOG_DIRECTORY + "application-" + date + ".log";
        File file = new File(fileName);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .body(resource);
    }
}
