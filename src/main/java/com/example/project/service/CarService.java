package com.example.project.service;

import com.example.project.model.Car;
import java.util.List;

public interface CarService {
    List<Car> showAllCars();

    Car saveCar(Car car);

    Car findById(int id);

    Car updateCar(Car car);

    void deleteCar(int id);

}
