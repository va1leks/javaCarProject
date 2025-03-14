package com.example.project.controllers;


import com.example.project.dto.create.DealershipDTO;
import com.example.project.model.Dealership;
import com.example.project.service.DealershipService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dealerships")
@AllArgsConstructor
public class DealershipController {

    private DealershipService dealershipService;

    @GetMapping("/{dealershipId}")
    public Dealership findDealershipById(@PathVariable Long dealershipId) {
        return dealershipService.findDealershipById(dealershipId);
    }

    @GetMapping
    public List<Dealership> findAllDealerships() {
        return dealershipService.findAllDealerships();
    }

    @PostMapping
    public Dealership createDealership(@RequestBody DealershipDTO dealership) {
        return dealershipService.saveDealership(dealership);
    }

    @PutMapping("updateDealership")
    public Dealership updateDealership(@RequestBody Dealership dealership) {
        return dealershipService.saveDealership(dealership);
    }

    @PutMapping("addCarToDealership/{dealershipId}")
    public Dealership updateDealership(@PathVariable Long dealershipId, @RequestBody Long carId) {
        return dealershipService.addCar(dealershipId, carId);
    }

    @DeleteMapping("deleteDealership/{dealershipId}")
    public void deleteDealership(@PathVariable Long dealershipId) {
        dealershipService.deleteDealership(dealershipId);
    }
}
