package com.example.project.controllers;


import com.example.project.service.impl.VisitCounterService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class VisitController {

    private VisitCounterService visitCounterService;

    @GetMapping("/visits")
    public Map<String, Integer> getAllVisitCounts() {
        return visitCounterService.getAllVisitCounts();
    }
}