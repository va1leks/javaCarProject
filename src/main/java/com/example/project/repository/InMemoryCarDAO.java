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
                .description("").price(20000).model("X5").build());
    }

    public List<Car> showAllCars() {
        return cars;
    }


    public Car saveCar(Car car) {
        return car;
    }


    public Car findById(int id) {
        return cars.stream().filter(car -> car.getId() == id).findFirst().orElse(null);
    }

    //todo
    public Car updateCar(Car car) {
        return null;
    }

    //todo
    public void deleteCar(int id) {

    }
}
