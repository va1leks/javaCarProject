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
                .description("спортивный внедорожник премиум класса.")
                .price(10000).model("X5").build());

        cars.add(Car.builder().id(2).brand("RAM").year(2020)
                .description("это первый пикап RAM, оснащенный системой настройки лаунчконтроля.")
                .price(90000).model("1500 TRX").build());

        cars.add(Car.builder().id(3).brand("Toyota").year(2013)
                .description("Надежный и комфортный седан бизнес-класса с экономичным двигателем.")
                .price(6000).model("Camry").build());

        cars.add(Car.builder().id(4).brand("Mercedes-Benz").year(2012)
                .description("Элегантный седан премиум-класса с передовыми технологиями.")
                .price(7000).model("C-class").build());

        cars.add(Car.builder().id(5).brand("Toyota").year(2010)
                .description("Мощный, надежный пикап, идеально подходящий для работы и бездорожья.")
                .price(15000).model("Tundra").build());
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
