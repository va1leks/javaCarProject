package com.example.project.service;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.model.Dealership;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DealershipService {

    Dealership findDealershipById(Long id);

    List<Dealership> findAllDealerships();
    @Transactional
    Dealership saveDealership(Dealership dealership);
    @Transactional
    Dealership saveDealership(DealershipDTO dealershipDTO);
    @Transactional
    Dealership updateDealership(Dealership dealership);

    Dealership addCar(Long carId, Long dealershipId);
    @Transactional
    void deleteDealership(Long id);
}
