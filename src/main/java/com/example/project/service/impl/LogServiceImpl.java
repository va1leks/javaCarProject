package com.example.project.service.impl;


import com.example.project.constant.LogTaskStatus;
import com.example.project.dto.get.GetLogStatusDTO;
import com.example.project.dto.get.GetRedyLogDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class LogServiceImpl {

    private final LogAsyncServiceImpl logAsyncService;
    private static final DateTimeFormatter LOG_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Map<UUID, LogTaskStatus> taskStatuses = new ConcurrentHashMap<>();
    private final Map<UUID, Path> taskFiles = new ConcurrentHashMap<>();


    @SneakyThrows
    public GetLogStatusDTO createLogTask(String date) {
        UUID taskId = UUID.randomUUID();
        taskStatuses.put(taskId, LogTaskStatus.IN_PROGRESS);
        logAsyncService.processLogTask(taskId, date, taskStatuses, taskFiles, LOG_DATE_FORMAT);

        return  GetLogStatusDTO.builder().taskId(taskId).url("get status: http://localhost:8080/api/v1/logs/status/" + taskId).build();
    }

    public GetRedyLogDTO getTaskStatus(UUID taskId) {

        if (taskStatuses.getOrDefault(taskId, LogTaskStatus.FAILED) == LogTaskStatus.DONE) {
            return GetRedyLogDTO.builder().taskStatus(taskStatuses.getOrDefault(taskId,
                            LogTaskStatus.FAILED))
                    .url("download log file: http://localhost:8080/api/v1/logs/file/" + taskId).build();
        }

        return GetRedyLogDTO.builder().taskStatus(taskStatuses.getOrDefault(taskId,
                        LogTaskStatus.FAILED))
                .url("").build();
    }

    public Resource getLogFile(UUID taskId) throws IOException {
        Path filePath = taskFiles.get(taskId);
        if (filePath == null || !Files.exists(filePath)) {
            throw new IOException("Log file not found");
        }
        return new UrlResource(filePath.toUri());
    }
}
