package com.example.project.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class VisitCounterService {

    private final ConcurrentHashMap<String, AtomicInteger> visitCounts = new ConcurrentHashMap<>();

    public synchronized void incrementVisit(String url) {
        visitCounts.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
        log.info("Visit recorded for URL: {}", url);
    }

    public Map<String, Integer> getAllVisitCounts() {
        return visitCounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }
}
