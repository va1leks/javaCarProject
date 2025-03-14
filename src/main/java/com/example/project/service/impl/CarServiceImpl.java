package com.example.project.service.impl;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;

import com.example.project.mappers.CarMapper;
import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import com.example.project.service.CarService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public List<GetCarDTO> showCars() {
        return carMapper.toDtos(carRepository.findAll());
    }

    @Override
    public List<Car> findCarByParam(String brand, String model, String year, int color) {

        if(brand == null || model == null)
            return carRepository.findAll();
        return carRepository.findByBrandAndModel(brand, model);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car saveCar(CarDTO carDTO) {
        Car car = Car.builder().brand(carDTO.getBrand()).model(carDTO.getModel())
                .year(carDTO.getYear()).color(carDTO.getColor()).price(carDTO.getPrice()
                ).mileage(carDTO.getMileage()).vin(carDTO.getVin()).status(carDTO.getStatus())
                .transmission(carDTO.getTransmission()).engineType(carDTO.getEngineType()).build();
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car car) {
        return carRepository.save(car);
    }


    @Override
    @Transactional
    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }


}
