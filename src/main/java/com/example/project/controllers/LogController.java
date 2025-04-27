package com.example.project.controllers;

import com.example.project.dto.get.GetLogStatusDTO;
import com.example.project.dto.get.GetRedyLogDTO;
import com.example.project.service.impl.LogServiceImpl;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/logs")
@AllArgsConstructor
public class LogController {

    private final LogServiceImpl logService;

    @PostMapping("/{date}")
    public GetLogStatusDTO requestLogFile(@PathVariable String date) {
        return logService.createLogTask(date);
    }

    @GetMapping("/status/{taskId}")
    public GetRedyLogDTO getLogTaskStatus(@PathVariable UUID taskId) {
        return logService.getTaskStatus(taskId);
    }

    @GetMapping("/file/{taskId}")
    public ResponseEntity<Resource> downloadLogFile(@PathVariable UUID taskId) {
        try {
            Resource resource = logService.getLogFile(taskId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + resource.getFilename())
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
