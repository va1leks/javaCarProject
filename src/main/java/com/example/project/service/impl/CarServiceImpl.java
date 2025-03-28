package com.example.project.service.impl;


import com.example.project.cache.MyCache;
import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.exeption.ErrorMessages;
import com.example.project.exeption.ResourceNotFoundException;
import com.example.project.mappers.CarMapper;
import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.CarService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
@Primary
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final DealershipRepository dealershipRepository;
    private final MyCache<Long, GetCarDTO> carCache = new MyCache<>(60000);

    @Override
    @Transactional
    public List<GetCarDTO> getCarsByDealership(Long dealershipId) {
        if (carCache.containsKey(dealershipId)) {
            return Collections.singletonList(carCache.get(dealershipId));
        }
        List<GetCarDTO> cars = carMapper.toDtos(carRepository.findByDealershipId(dealershipId));
        carCache.put(dealershipId, (GetCarDTO) cars);

        if (cars.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return cars;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetCarDTO findById(Long id) {
        if (carCache.containsKey(id)) {
            return carCache.get(id);
        }
        GetCarDTO carDto = carMapper.toDto(carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CAR_NOT_FOUND, id))));
        carCache.put(id, carDto);
        return carDto;
    }

    @Override
    @Transactional
    public List<GetCarDTO> showCars() {
        return carMapper.toDtos(carRepository.findAll());
    }

    @Override
    @Transactional
    public GetCarDTO saveCar(CarDTO carDto) {
        GetCarDTO savedCar = carMapper.toDto(carRepository
                .save(carMapper.toEntity(carDto, dealershipRepository)));
        carCache.put(savedCar.getId(), savedCar);
        return savedCar;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetCarDTO updateCar(Car car) {
        carRepository.findById(car.getId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.CAR_NOT_FOUND, car.getId())));
        GetCarDTO updatedCar = carMapper.toDto(carRepository.save(car));
        carCache.put(updatedCar.getId(), updatedCar);
        return updatedCar;
    }

    @Override
    @Transactional
    @SneakyThrows
    public GetCarDTO patchCar(PatchCarDTO patchCarDto, Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CAR_NOT_FOUND, id)));
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
        GetCarDTO patchedCar = carMapper.toDto(carRepository.save(car));
        carCache.put(patchedCar.getId(), patchedCar);
        return patchedCar;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.CAR_NOT_FOUND, carId)));
        carRepository.delete(car);
        carCache.getCache().remove(carId);
    }
}
