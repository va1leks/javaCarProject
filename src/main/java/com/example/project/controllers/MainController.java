package com.example.project.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    // GET с Query Parameters
    // Пример запроса: /api/cars?brand=Toyota&model=Camry

    @GetMapping("/api/cars")
    public Map<String, Object> getCars(
             @RequestParam(required = false) String brand,
             @RequestParam(required = false) String model) {

        Map<String, Object> car1 = new HashMap<>();
        car1.put("id", 1);
        car1.put("brand", "Toyota");
        car1.put("model", "Camry");
        car1.put("year", 2020);
        car1.put("price", 25000);

        Map<String, Object> car2 = new HashMap<>();
        car2.put("id", 2);
        car2.put("brand", "Toyota");
        car2.put("model", "Corolla");
        car2.put("year", 2019);
        car2.put("price", 20000);

        List<Map<String, Object>> sameCars = new ArrayList<>();

        sameCars.add(car1);
        sameCars.add(car2);

        // Фильтрация по brand и model (если указаны)
        if (brand != null) {
            sameCars.removeIf(car -> !car.get("brand").equals(brand));
        }
        if (model != null) {
            sameCars.removeIf(car -> !car.get("model").equals(model));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", sameCars);

        return response;
    }

    // GET с Path Parameters
    // Пример запроса: /api/cars/1
    @GetMapping("/api/cars/{id}")
    public Map<String, Object> getCarById(@PathVariable int id) {

        Map<String, Object> car1 = new HashMap<>();
        car1.put("id", 1);
        car1.put("brand", "Toyota");
        car1.put("model", "Land Cruiser");
        car1.put("year", 2020);
        car1.put("price", 25000);
        car1.put("description", "Комфортный седан с отличной управляемостью.");

        Map<String, Object> car2 = new HashMap<>();
        car2.put("id", 2);
        car2.put("brand", "BMW");
        car2.put("model", "X5");
        car2.put("year", 2022);
        car2.put("price", 50000);
        car2.put("description", "среднеразмерный кроссовер от немецкого автопроизводителя BMW");

        List<Map<String, Object>> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);

        if (id != 0) {
            cars.removeIf(car -> !car.get("id").equals(id));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", cars);

        return response;
    }
}
