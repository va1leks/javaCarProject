package com.example.project.service;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.model.Car;
import java.util.List;

public interface CarService {

    List<GetCarDTO> getCarsByDealershipName(String dealershipName);

    GetCarDTO findById(Long id);

    List<GetCarDTO> saveAllCars(List<CarDTO> carDtos);

    List<GetCarDTO> showCars();

    GetCarDTO saveCar(CarDTO car);

    GetCarDTO updateCar(UpdateCarDto car, Long id);

    GetCarDTO patchCar(UpdateCarDto car, Long id);

    void deleteCar(Long carId);

    List<GetCarDTO> getCarsByDealership(Long dealershipId);
}
