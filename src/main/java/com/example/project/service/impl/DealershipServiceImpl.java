package com.example.project.service.impl;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Primary
public class DealershipServiceImpl implements DealershipService {

    private final DealershipRepository dealershipRepository;
    private final CarRepository carRepository;
    private final DealershipMapper dealershipMapper;


    @Override
    public GetDealershipDTO findDealershipById(Long id) {
        return dealershipMapper.toDto(dealershipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dealership not found")));
    }



    @Override
    public List<GetDealershipDTO> findAllDealerships() {
        List<Dealership> dealerships = (List<Dealership>) dealershipRepository.findAll();
        if (dealerships.isEmpty()) {
            throw new EntityNotFoundException("dealerships not found");
        }
        return dealershipMapper.toDtos(dealerships);
    }

    @Override
    public Dealership saveDealership(DealershipDTO dealershipDto) {
        Dealership dealership = Dealership.builder().name(dealershipDto
                .getName()).address(dealershipDto.getAddress()).build();
        return dealershipRepository.save(dealership);
    }

    @Override
    public GetDealershipDTO updateDealership(Dealership dealership) {
        return dealershipMapper.toDto(dealershipRepository.save(dealership));
    }

    @Override
    @Transactional
    public  GetDealershipDTO addCar(Long dealershipId, Long carId) {
        Dealership dealership = dealershipRepository.findById(dealershipId)
                .orElseThrow(() -> new EntityNotFoundException("dealership not found"));
        Car car = carRepository.findById(carId).orElseThrow(()
                -> new EntityNotFoundException("car not found"));
        car.setDealership(dealership);
        carRepository.save(car);
        dealership.getCars().add(car);

        return dealershipMapper.toDto(dealershipRepository.save(dealership));
    }


    @Override
    public void deleteDealership(Long id) {
        dealershipRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GetDealershipDTO deleteCar(Long dealershipId, Long carId) {
        Dealership dealership = dealershipRepository.findById(dealershipId).orElseThrow();
        Car car = carRepository.findById(carId).orElseThrow();
        car.setDealership(null);
        carRepository.save(car);
        dealership.getCars().remove(carRepository.findById(carId).orElseThrow());
        return dealershipMapper.toDto(dealershipRepository.save(dealership));
    }
}
