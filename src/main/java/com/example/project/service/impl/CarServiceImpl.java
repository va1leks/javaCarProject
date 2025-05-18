package com.example.project.service.impl;

import com.example.project.cache.MyCache;
import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.exception.ErrorMessages;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.exception.ValidError;
import com.example.project.mappers.CarMapper;
import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.CarService;
import jakarta.transaction.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@AllArgsConstructor
@Primary
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final DealershipRepository dealershipRepository;
    private final MyCache<Long, GetCarDTO> carCache = new MyCache<>(60000, 500);

    @Override
    public void removeCarFromCache(Long carId) {
        if (carCache.containsKey(carId)) {
            log.info("Removing car with ID {} from cache", carId);
            carCache.remove(carId);
        }
    }

    @Override
    public void addCarFromCache(GetCarDTO carDTO) {
            log.info("add car with ID {} from cache", carDTO.getId());
            carCache.put(carDTO.getId(),carDTO);
    }

    @Override
    @Transactional
    public List<GetCarDTO> getCarsByDealership(Long dealershipId) {
        List<GetCarDTO>  cars = carCache.getCache().values().stream()
                .filter(car -> car.getDealershipId() != null
                        && dealershipId.equals(car.getDealershipId().getId()))
                .collect(Collectors.toList());

        if (!cars.isEmpty() && (cars.size() == carRepository.findByDealershipId(dealershipId).size())) {
            log.info("Cache hit for Dealership ID: {}", dealershipId);
            return cars;
        }
        cars = carMapper.toDtos(carRepository.findByDealershipId(dealershipId));
        if (cars.isEmpty()) {
            return null;
        }
        cars.forEach(car -> carCache.put(car.getId(), car));


        return cars;
    }

    @Override
    @Transactional
    public List<GetCarDTO> getCarsByDealershipName(String dealershipName) {
        List<GetCarDTO> cars = carCache.getCache().values().stream()
                .filter(car -> car.getDealershipId() != null
                        && dealershipName.equalsIgnoreCase(car.getDealershipId().getName()))
                .collect(Collectors.toList());

        if (!cars.isEmpty()) {
            log.info("Cache hit for Dealership Name: {}", dealershipName);
            return cars;
        }
        cars = carMapper.toDtos(carRepository.findByDealershipName(dealershipName));
        cars.forEach(car -> carCache.put(car.getId(), car));

        if (cars.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No cars found for dealership: %s".formatted(dealershipName));
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
        GetCarDTO carDto = carMapper.toGetDto(carRepository.findById(id)
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

    @SneakyThrows
    @Override
    @Transactional
    public GetCarDTO saveCar(CarDTO carDto) {
        if(carRepository.findByVin(carDto.getVin())!=null){
            Map<String, String> errors = new HashMap<>();
            errors.put("vin", "Car with this VIN already exists");
            throw new ValidError(HttpStatus.BAD_REQUEST, errors);
        }
        GetCarDTO savedCar = carMapper.toGetDto(carRepository
                .save(carMapper.toEntity(carDto, dealershipRepository)));
        carCache.put(savedCar.getId(), savedCar);
        return savedCar;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetCarDTO updateCar(UpdateCarDto car,Long id) {
        carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.CAR_NOT_FOUND, id)));
        if((carRepository.findByVin(car.getVin()) != null) &&
                !carRepository.findById(id).get().getVin().equals(car.getVin())){
            Map<String, String> errors = new HashMap<>();
            errors.put("vin", "Car with this VIN already exists");
            throw new ValidError(HttpStatus.BAD_REQUEST, errors);
        }
        Car SaveCar = carMapper.toEntityFromUpdateCarDTO(car);
        SaveCar.setId(id);
        carRepository.save(SaveCar);
        carCache.put(id,carMapper.toGetDto(SaveCar));
        return carMapper.toGetDto(SaveCar);
    }

    @Override
    @Transactional
    @SneakyThrows
    public GetCarDTO patchCar(UpdateCarDto patchCarDto, Long id) {
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
        GetCarDTO patchedCar = carMapper.toGetDto(carRepository.save(car));
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

        if (carCache.containsKey(car.getId())) {
            log.info("delete car {} from cache", carId);
        }
    }

    public List<GetCarDTO> saveAllCars(List<CarDTO> carDtos) {
        return carDtos.stream()
                .map(dto -> carMapper.toEntity(dto, dealershipRepository))
                .map(carRepository::save)
                .map(carMapper::toGetDto)
                .toList();
    }


}
