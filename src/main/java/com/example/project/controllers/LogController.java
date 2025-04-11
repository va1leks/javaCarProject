package com.example.project.controllers;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/logs")
public class LogController {

    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/{date}")
    public ResponseEntity<Resource> getLogsByDate(@PathVariable String date) throws IOException {
        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        String datePrefix = targetDate.format(LOG_DATE_FORMAT);

        Path logDir = Paths.get("logs");
        if (!Files.exists(logDir)) {
            return ResponseEntity.notFound().build();
        }

        List<String> filteredLines;
        try (Stream<Path> logFiles = Files.list(logDir)) {
            filteredLines = logFiles
                    .filter(path -> path.toString().endsWith(".log"))
                    .flatMap(path -> {
                        try {
                            return Files.lines(path);
                        } catch (IOException e) {
                            return Stream.empty();
                        }
                    })
                    .filter(line -> line.startsWith(datePrefix))
                    .toList();
        }

        if (filteredLines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path tempLogFile = Files.createTempFile(tempDir, "logs-" + date + "-", ".log");
        Files.write(tempLogFile, filteredLines);

        Resource resource = new UrlResource(tempLogFile.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs-" + date + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
