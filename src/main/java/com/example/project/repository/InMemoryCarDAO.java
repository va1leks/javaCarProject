package com.example.project.repository;

import com.example.project.model.Car;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;


@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Repository
public class InMemoryCarDAO {

    private final List<Car> cars = new ArrayList<>();

    InMemoryCarDAO() {
        cars.add(Car.builder().id(1).brand("BMW").year(2005)
                .description("").price(20000).model("X5").build());

        cars.add(Car.builder().id(2).brand("Mrs").year(2005)
                .description("").price(20000).model("X5").build());

        cars.add(Car.builder().id(3).brand("Toyota").year(2005)
                .description("").price(20000).model("Camry").build());

        cars.add(Car.builder().id(4).brand("Toyota").year(2005)
                .description("").price(20000).model("X5").build());

        cars.add(Car.builder().id(5).brand("Toyota").year(2005)
                .description("").price(20000).model("X5").build());
    }

    public List<Car> showCars(String brand, String model) {
        return  cars.stream()
                .filter(car -> brand == null || car.getBrand().equals(brand))
                .filter(car -> model == null || car.getModel().equals(model))
                .toList();

    }

    public Car findById(int id) {
        return cars.stream().filter(car -> car.getId() == id).findFirst().orElse(null);
    }
}
