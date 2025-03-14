package com.example.project.repository;

import com.example.project.model.Car;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


import org.springframework.stereotype.Repository;


@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Repository
public class InMemoryCarDAO {

    private final List<Car> cars = new ArrayList<>();

    public List<Car> showCars() {
        return cars;
    }
    public List<Car> findCarByParam(String brand, String model, String year, int color) {
        return  cars.stream()
                .filter(car -> brand == null || car.getBrand().equals(brand))
                .filter(car -> model == null || car.getModel().equals(model))
                .toList();

    }

    public Car findById(Long id) {
        return cars.stream().filter(car -> Objects.equals(car.getId(), id)).findFirst().orElse(null);
    }


    public Car saveCar(Car car) {
        cars.add(car);
        return car;
    }


    public Car updateCar(Car car) {
        int carIndex = IntStream.range(0,cars.size())
                .filter(index -> cars.get(index).getId().equals(car.getId())).findFirst().orElse(-1);

        if(carIndex > -1) {
            cars.set(carIndex,car);
            return car;
        }
        return null;

    }


    public void deleteCar(Long carId) {
        cars.removeIf(car -> Objects.equals(car.getId(), carId));

    }
}
