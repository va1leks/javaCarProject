package com.example.project.controllers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.model.Car;
import com.example.project.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping("{id}")
    public GetCarDTO findCarById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @GetMapping
    public List<GetCarDTO> findAllCars() {
        return carService.showCars();
    }

    @PostMapping
    public GetCarDTO saveCar(@RequestBody CarDTO car) {
        return carService.saveCar(car);
    }

    @PutMapping("updateCar")
    public GetCarDTO updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("deleteCar/{carId}")
    public void deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetCarDTO> patchCar(@PathVariable Long id,
                                              @RequestBody PatchCarDTO patchCarDto) {
        GetCarDTO updatedCar = carService.patchCar(patchCarDto, id);
        return ResponseEntity.ok(updatedCar);
    }
}
