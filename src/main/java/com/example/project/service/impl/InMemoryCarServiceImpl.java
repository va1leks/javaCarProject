package com.example.project.service.impl;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.model.Car;
import com.example.project.repository.InMemoryCarDAO;
import com.example.project.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryCarServiceImpl implements CarService {

    private final InMemoryCarDAO repository;

    @Override
    public Car findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> findCarByParam(String brand, String model, String year, int color) {

        return repository.findCarByParam(brand, model,year,color);
    }

    @Override
    public List<GetCarDTO> showCars() {
        return null;
    }

    @Override
    public Car saveCar(Car car) {
        return repository.saveCar(car);
    }

    @Override
    public Car saveCar(CarDTO car) {
        return null;
    }

    @Override
    public Car updateCar(Car car) {
        return repository.updateCar(car);
    }

    @Override
    public void deleteCar(Long carId) {
        repository.deleteCar(carId);
    }
}
