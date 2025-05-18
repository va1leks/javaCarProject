package com.example.project.service.impl;

import com.example.project.cache.MyCache;
import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.exception.ErrorMessages;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.CarMapper;
import com.example.project.mappers.DealershipMapper;
import com.example.project.model.Car;
import com.example.project.model.Dealership;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.DealershipService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class DealershipServiceImpl implements DealershipService {

    private final DealershipRepository dealershipRepository;
    private final CarRepository carRepository;
    private final DealershipMapper dealershipMapper;
    private final CarServiceImpl carService;
    private final MyCache<Long, GetDealershipDTO> dealershipCache = new MyCache<>(60000, 500);
    private final CarMapper carMapper;

    @SneakyThrows
    @Override
    @Transactional
    public GetDealershipDTO findDealershipById(Long id) {
        if (dealershipCache.containsKey(id)) {
            return dealershipCache.get(id);
        }
        GetDealershipDTO dealershipDto = dealershipMapper.toDto(dealershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id))));
        dealershipCache.put(id, dealershipDto);
        return dealershipDto;
    }

    @Override
    @Transactional
    public List<GetDealershipDTO> findAllDealerships() {
        List<Dealership> dealerships = (List<Dealership>) dealershipRepository.findAll();
        if (dealerships.isEmpty()) {
            return null;
        }
        return dealershipMapper.toDtos(dealerships);
    }

    @Override
    @Transactional
    public Dealership saveDealership(DealershipDTO dealershipDto) {
        Dealership dealership = Dealership.builder().name(dealershipDto.getName())
                .address(dealershipDto.getAddress()).build();
        Dealership savedDealership = dealershipRepository.save(dealership);

        dealershipCache.put(savedDealership.getId(), dealershipMapper.toDto(savedDealership));
        return savedDealership;
    }

    @Override
    @Transactional
    public GetDealershipDTO updateDealership(Dealership dealership) {
        Dealership updatedDealership = dealershipRepository.save(dealership);
        GetDealershipDTO updatedDto = dealershipMapper.toDto(updatedDealership);

        dealershipCache.put(updatedDealership.getId(), updatedDto);
        return updatedDto;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetDealershipDTO addCar(Long dealershipId, Long carId) {
        Dealership dealership = dealershipRepository.findById(dealershipId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.DEALERSHIP_NOT_FOUND, dealershipId)));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, carId)));

        car.setDealership(dealership);
        carRepository.save(car);
        dealership.getCars().add(car);
        Dealership updatedDealership = dealershipRepository.save(dealership);
        GetDealershipDTO updatedDto = dealershipMapper.toDto(updatedDealership);
        dealershipCache.put(dealershipId, updatedDto);
        carService.addCarFromCache(carMapper.toGetDto(carRepository.findById(carId)));

        return updatedDto;
    }

    @Override
    @Transactional
    public void deleteDealership(Long id) {
        dealershipRepository.deleteById(id);
        dealershipCache.getCache().remove(id);
    }

    @Override
    @Transactional
    public GetDealershipDTO deleteCar(Long dealershipId, Long carId) {
        Dealership dealership = dealershipRepository.findById(dealershipId).orElseThrow();
        Car car = carRepository.findById(carId).orElseThrow();

        car.setDealership(null);
        carRepository.save(car);
        dealership.getCars().remove(carRepository.findById(carId).orElseThrow());
        Dealership updatedDealership = dealershipRepository.save(dealership);
        GetDealershipDTO updatedDto = dealershipMapper.toDto(updatedDealership);
        dealershipCache.remove(dealershipId);
        carService.removeCarFromCache(carId);
        return updatedDto;
    }
}
