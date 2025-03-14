package com.example.project.service.impl;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.model.Car;
import com.example.project.model.Dealership;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.DealershipService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class DealershipServiceImpl implements DealershipService {

    private final DealershipRepository dealershipRepository;
    private final CarRepository carRepository;

    @Override
    public Dealership findDealershipById(Long id) {
        return dealershipRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dealership> findAllDealerships() {
        return (List<Dealership>) dealershipRepository.findAll();
    }

    @Override
    public Dealership saveDealership(Dealership dealership) {
        return dealershipRepository.save(dealership);
    }

    @Override
    public Dealership saveDealership(DealershipDTO dealershipDTO) {
        Dealership dealership = Dealership.builder().name(dealershipDTO
                .getName()).address(dealershipDTO.getAddress()).build();
        return dealershipRepository.save(dealership);
    }

    @Override
    public Dealership updateDealership(Dealership dealership) {
        return dealershipRepository.save(dealership);
    }

    @Override
    public  Dealership addCar(Long dealershipId, Long carId) {
        Dealership dealership = dealershipRepository.findById(dealershipId).orElseThrow();
        Car car = carRepository.findById(carId).orElseThrow();
        dealership.getCars().add(car);
        return dealershipRepository.save(dealership);
    }


    @Override
    public void deleteDealership(Long id) {
        dealershipRepository.deleteById(id);
    }
}
