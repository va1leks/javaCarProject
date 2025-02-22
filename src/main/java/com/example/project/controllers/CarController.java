package com.example.project.controllers;

import com.example.project.model.Car;
import com.example.project.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping
    public List<Car> showAllCars() {
        return carService.showAllCars();
    }

    @GetMapping("/{id}")
    public Car findCarById(@PathVariable int id) {
        return carService.findById(id);
    }
}
