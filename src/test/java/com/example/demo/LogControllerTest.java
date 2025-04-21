package com.example.demo;

import com.example.project.controllers.LogController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogControllerTest {

    private LogController logController;
    private Path logsDir;

    @BeforeEach
    void setUp() throws IOException {
        logController = new LogController();
        // Создаём директорию logs в рабочей директории
        logsDir = Paths.get("").toAbsolutePath().resolve("logs");
        Files.createDirectories(logsDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем директорию logs после каждого теста
        if (Files.exists(logsDir)) {
            Files.walk(logsDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {}
                    });
        }
    }

    @Test
    void getLogsByDateShouldReturnFilteredLogs() throws IOException {
        String date = LocalDate.now().toString();

        Path logFile = logsDir.resolve("test.log");
        List<String> lines = List.of(
                date + " INFO: This is today's log",
                "2024-01-01 INFO: Old log"
        );
        Files.write(logFile, lines);

        ResponseEntity<Resource> response = logController.getLogsByDate(date);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        String content = new String(response.getBody().getInputStream().readAllBytes());
        assertTrue(content.contains("This is today's log"));
        assertFalse(content.contains("Old log"));
    }

    @Test
    void getLogsByDateShouldReturnNoContentIfNoMatchingLines() throws IOException {
        String date = LocalDate.now().toString();

        Path logFile = logsDir.resolve("test.log");
        Files.write(logFile, List.of("2023-01-01 INFO: Another log"));

        ResponseEntity<Resource> response = logController.getLogsByDate(date);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getLogsByDateShouldReturnNotFoundIfLogDirMissing() throws IOException {
        // Удаляем логовую директорию
        Files.walk(logsDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {}
                });

        String date = LocalDate.now().toString();

        ResponseEntity<Resource> response = logController.getLogsByDate(date);

        assertEquals(404, response.getStatusCodeValue());
    }
}
