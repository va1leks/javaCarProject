package com.example.project.service;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.model.Car;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CarService {


    Object findById(Long id);

    List<GetCarDTO> showCars();

    List<Car> findCarByParam(String brand, String model, String year, int color);
    @Transactional
    Car saveCar(Car car);
    @Transactional
    Car saveCar(CarDTO car);
    @Transactional
    Car updateCar(Car car);
    @Transactional
    void deleteCar(Long carId);
}
