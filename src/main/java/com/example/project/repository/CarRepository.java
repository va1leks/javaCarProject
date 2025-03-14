package com.example.project.repository;

import com.example.project.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByBrandAndModel(String brand, String model);

}
