package com.example.project.controllers;

import com.example.project.exeption.CarNotFoundException;
import com.example.project.model.Car;
import com.example.project.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping("/{id}")
    public Car findCarById(@PathVariable int id) throws CarNotFoundException {
        Car car = carService.findById(id);
        if (car == null) {
            throw new CarNotFoundException(" car not found, id: " + id);
        }
        return car;
    }

    @GetMapping
    public List<Car> showCarsByParameters(@RequestParam(required = false) String brand,
                                          @RequestParam(required = false) String model)
                                          throws CarNotFoundException {
        List<Car> cars = carService.showCars(brand, model);
        if (cars.isEmpty()) {
            throw new CarNotFoundException(" car not found, brand , model: "
                                            + brand + ", " + model);
        }
        return cars;
    }
}
