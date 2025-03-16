package com.example.project.service;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Dealership;
import java.util.List;

public interface DealershipService {

    GetDealershipDTO findDealershipById(Long id);

    List<GetDealershipDTO> findAllDealerships();

    Dealership saveDealership(DealershipDTO dealershipDto);

    GetDealershipDTO updateDealership(Dealership dealership);

    GetDealershipDTO addCar(Long carId, Long dealershipId);

    void deleteDealership(Long id);

    GetDealershipDTO deleteCar(Long dealershipId, Long carId);
}
