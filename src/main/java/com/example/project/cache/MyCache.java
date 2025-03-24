package com.example.project.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Data;

@Data
public class MyCache<K, V> {

    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final long maxAgeInMillis;

    public MyCache(long maxAgeInMillis) {
        this.maxAgeInMillis = maxAgeInMillis;
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
        executor.schedule(() -> cache.remove(key), maxAgeInMillis, TimeUnit.MILLISECONDS);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}
