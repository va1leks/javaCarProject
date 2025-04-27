package com.example.project.service.impl;

import com.example.project.constant.LogTaskStatus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class LogAsyncServiceImpl {
    @SneakyThrows
    @Async
    public CompletableFuture<Void> processLogTask(UUID taskId, String date,
                                                  Map<UUID, LogTaskStatus> taskStatuses,
                                                  Map<UUID, Path> taskFiles,
                                                  DateTimeFormatter dateFormat) {
        Thread.sleep(5000);
        try {
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            String datePrefix = targetDate.format(dateFormat);

            Path logDir = Paths.get("logs");
            if (!Files.exists(logDir)) {
                taskStatuses.put(taskId, LogTaskStatus.FAILED);
                return CompletableFuture.completedFuture(null);
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
                taskStatuses.put(taskId, LogTaskStatus.FAILED);
                return CompletableFuture.completedFuture(null);
            }

            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path tempLogFile = Files.createTempFile(tempDir, "logs-" + date + "-", ".log");
            Files.write(tempLogFile, filteredLines);

            taskFiles.put(taskId, tempLogFile);
            taskStatuses.put(taskId, LogTaskStatus.DONE);
        } catch (Exception e) {
            taskStatuses.put(taskId, LogTaskStatus.FAILED);
        }

        return CompletableFuture.completedFuture(null);
    }

}
