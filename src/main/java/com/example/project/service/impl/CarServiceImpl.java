package com.example.project.service.impl;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.mappers.CarMapper;
import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
@Primary
@Transactional
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final DealershipRepository dealershipRepository;

    @Override
    public GetCarDTO findById(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "car not found")));
    }

    @Override
    @Transactional
    public List<GetCarDTO> showCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cars not found");
        }
        return carMapper.toDtos(cars);
    }


    @Override
    @Transactional
    public GetCarDTO saveCar(CarDTO carDto) {
        return carMapper.toDto(carRepository.save(carMapper
                .toEntity(carDto, dealershipRepository)));
    }

    @Override
    public GetCarDTO updateCar(Car car) {
        Car car1 = carRepository.findById(car.getId()).orElseThrow(()
                -> new EntityNotFoundException("no car found"));
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public GetCarDTO patchCar(PatchCarDTO patchCarDto, Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no car found"));
        if (patchCarDto.getBrand() != null) {
            car.setBrand(patchCarDto.getBrand());
        }
        if (patchCarDto.getModel() != null) {
            car.setModel(patchCarDto.getModel());
        }
        if (patchCarDto.getYear() != null) {
            car.setYear(patchCarDto.getYear());
        }
        if (patchCarDto.getPrice() != null) {
            car.setPrice(patchCarDto.getPrice());
        }
        if (patchCarDto.getMileage() != null) {
            car.setMileage(patchCarDto.getMileage());
        }
        if (patchCarDto.getVin() != null) {
            car.setVin(patchCarDto.getVin());
        }
        if (patchCarDto.getStatus() != null) {
            car.setStatus(patchCarDto.getStatus());
        }
        if (patchCarDto.getTransmission() != null) {
            car.setTransmission(patchCarDto.getTransmission());
        }
        if (patchCarDto.getColor() != null) {
            car.setColor(patchCarDto.getColor());
        }
        if (patchCarDto.getEngineType() != null) {
            car.setEngineType(patchCarDto.getEngineType());
        }

        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("car not deleted"));
        carRepository.delete(car);
    }
}
