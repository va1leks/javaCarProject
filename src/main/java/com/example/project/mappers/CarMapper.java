package com.example.project.mappers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.get.GetUserDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.model.Car;
import com.example.project.model.User;
import com.example.project.model.Dealership;
import com.example.project.repository.DealershipRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class CarMapper {

    private DealershipMapper dealershipMapper;
    private DealershipRepository dealershipRepository;

    public Car toEntityFromUpdateCarDTO(UpdateCarDto carDTO) {
        if (carDTO == null) {
            return null; // Возвращаем null, если входной объект пуст
        }

        Car.CarBuilder carBuilder = Car.builder();

        if (carDTO.getBrand() != null) {
            carBuilder.brand(carDTO.getBrand());
        }
        if (carDTO.getModel() != null) {
            carBuilder.model(carDTO.getModel());
        }
        if (carDTO.getYear() != null) {
            carBuilder.year(carDTO.getYear());
        }
        if (carDTO.getPrice() != null) {
            carBuilder.price(carDTO.getPrice());
        }
        if (carDTO.getMileage() != null) {
            carBuilder.mileage(carDTO.getMileage());
        }
        if (carDTO.getVin() != null) {
            carBuilder.vin(carDTO.getVin());
        }
        if (carDTO.getStatus() != null) {
            carBuilder.status(carDTO.getStatus());
        }
        if (carDTO.getTransmission() != null) {
            carBuilder.transmission(carDTO.getTransmission());
        }
        if (carDTO.getColor() != null) {
            carBuilder.color(carDTO.getColor());
        }
        if (carDTO.getEngineType() != null) {
            carBuilder.engineType(carDTO.getEngineType());
        }
        if (carDTO.getDealershipName() != null) {
            carBuilder.dealership(dealershipRepository.findByName(carDTO.getDealershipName()));
        }


        return carBuilder.build();
    }


    public GetCarDTO toGetDto(Car car) {
        return GetCarDTO.builder()
                .dealershipId(dealershipMapper.toDto(car.getDealership()))
                .interestedUsers(this.mapClientsToDto(car.getInterestedUsers()))
                .id(car.getId())
                .model(car.getModel())
                .brand(car.getBrand())
                .color(car.getColor())
                .price(car.getPrice())
                .vin(car.getVin())
                .year(car.getYear())
                .mileage(car.getMileage())
                .engineType(car.getEngineType())
                .status(car.getStatus())
                .transmission(car.getTransmission())
                .build();
    }

    public List<GetCarDTO> toDtos(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return Collections.emptyList();
        }

        return cars.stream()
                .map(this::toGetDto)  // вызываем твой метод для каждого элемента
                .collect(Collectors.toList());
    }

    public List<Long> mapClientsToDto(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public Car toEntity(CarDTO carDto, DealershipRepository dealershipRepository) {
        if (carDto == null) {
            return null;
        }

        Car car = new Car();
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setYear(carDto.getYear());
        car.setPrice(carDto.getPrice());
        car.setMileage(carDto.getMileage());
        car.setVin(carDto.getVin());
        car.setStatus(carDto.getStatus());
        car.setTransmission(carDto.getTransmission());
        car.setColor(carDto.getColor());
        car.setEngineType(carDto.getEngineType());

        if (carDto.getDealership() != null) {
            Dealership dealership = dealershipRepository.findByName(carDto.getDealership());

            if (dealership != null) {
                car.setDealership(dealership);
            }
        }
                return car;
    }
}


