package com.example.project.service.impl;

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
    public Car findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Car> showCars(String brand, String model) {
        return repository.showCars(brand, model);
    }
}
