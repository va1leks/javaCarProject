package com.example.project.service;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.model.Car;
import java.util.List;

public interface CarService {

    GetCarDTO findById(Long id);

    List<GetCarDTO> showCars();

    GetCarDTO saveCar(CarDTO car);

    GetCarDTO updateCar(Car car);

    GetCarDTO patchCar(PatchCarDTO car, Long id);

    void deleteCar(Long carId);
}
