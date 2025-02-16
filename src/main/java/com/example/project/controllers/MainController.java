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
    private static final Map<String, Object> car1 = new HashMap<>();
    private static final Map<String, Object> car2 = new HashMap<>();
    private static final Map<String, Object> car3 = new HashMap<>();
    private static final Map<String, Object> car4 = new HashMap<>();

    public MainController() {
        String strBrand = "brand";
        String strModel = "model";
        String strPrice = "price";
        String strYear = "year";
        String sameBrand = "Toyota";

        car1.put("id", 1);
        car1.put(strBrand, sameBrand);
        car1.put(strModel, "Camry");
        car1.put(strYear, 2020);
        car1.put(strPrice, 25000);

        car2.put("id", 2);
        car2.put(strBrand, sameBrand);
        car2.put(strModel, "Corolla");
        car2.put(strYear, 2019);
        car2.put(strPrice, 20000);

        car3.put("id", 3);
        car3.put(strBrand, sameBrand);
        car3.put(strModel, "Land Cruiser");
        car3.put(strYear, 2020);
        car3.put(strPrice, 25000);
        car3.put("description", "Комфортный седан с отличной управляемостью.");

        car4.put("id", 4);
        car4.put(strBrand, "BMW");
        car4.put(strModel, "X5");
        car4.put(strYear, 2022);
        car4.put(strPrice, 50000);
        car4.put("description", "среднеразмерный кроссовер от немецкого автопроизводителя BMW");
    }

    @GetMapping("/api/cars")
    public Map<String, Object> getCars(
             @RequestParam(required = false) String brand,
             @RequestParam(required = false) String model) {

        List<Map<String, Object>> sameCars = new ArrayList<>();

        sameCars.add(car1);
        sameCars.add(car2);
        sameCars.add(car3);
        sameCars.add(car4);

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

        List<Map<String, Object>> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);

        if (id != 0) {
            cars.removeIf(car -> !car.get("id").equals(id));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", cars);

        return response;
    }
}
