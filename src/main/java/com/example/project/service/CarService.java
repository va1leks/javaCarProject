package com.example.project.service;

import com.example.project.model.Car;
import java.util.List;

public interface CarService {

    Car findById(int id);

    List<Car> showCars(String brand, String model);

}
