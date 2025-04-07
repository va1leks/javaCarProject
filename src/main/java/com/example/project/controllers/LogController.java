package com.example.project.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/logs")
public class LogController {

    private static final String LOG_FILE_PATH = "logs/application.log";
    private static final DateTimeFormatter LOG_DATE_FORMAT
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/{date}")
    public ResponseEntity<Resource> getLogsByDate(@PathVariable String date) throws IOException {

        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        Path logPath = Paths.get(LOG_FILE_PATH);
        if (!Files.exists(logPath)) {
            return ResponseEntity.notFound().build();
        }

        List<String> filteredLines;
        try (Stream<String> lines = Files.lines(logPath)) {
            filteredLines = lines
                    .filter(line -> line.startsWith(targetDate.format(LOG_DATE_FORMAT)))
                    .collect(Collectors.toList());
        }

        if (filteredLines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path tempLogFile = Files.createTempFile(tempDir, "logs-" + date + "-", ".log");
        Files.write(tempLogFile, filteredLines);

        Resource resource = new UrlResource(tempLogFile.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs-"
                        + date + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
