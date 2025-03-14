package com.example.project.controllers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.exeption.CarNotFoundException;
import com.example.project.model.Car;
import com.example.project.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping("{id}")
    public Car findCarById(@PathVariable Long id) throws CarNotFoundException {
        Car car = (Car) carService.findById(id);
        if (car == null) {
            throw new CarNotFoundException(" car not found, id: " + id);
        }
        return car;
    }

    @GetMapping
    public List<GetCarDTO> findAllCars() {
        return carService.showCars();
    }

    @GetMapping("param")
    public List<Car> showCarsByParameters(@RequestParam(required = false) String brand,
                                          @RequestParam(required = false) String model,
                                          @RequestParam(required = false) int year,
                                          @RequestParam(required = false) String color)
                                          throws CarNotFoundException {

        List<Car> cars = carService.findCarByParam(brand, model, color,year);
        if (cars.isEmpty()) {
            throw new CarNotFoundException(" car not found, brand , model: "
                                            + brand + ", " + model);
        }
        return cars;
    }

    @PostMapping("saveCar")
    public Car saveCar(@RequestBody CarDTO car) {
        return carService.saveCar(car);
    }

    @PutMapping("updateCar")
    public Car updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("deleteCar/{carId}")
    public void deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
    }
}
