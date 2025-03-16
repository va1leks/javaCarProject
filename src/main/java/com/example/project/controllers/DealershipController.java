package com.example.project.controllers;


import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Dealership;
import com.example.project.service.DealershipService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/dealerships")
@AllArgsConstructor
public class DealershipController {

    private DealershipService dealershipService;

    @GetMapping("/{dealershipId}")
    public GetDealershipDTO findDealershipById(@PathVariable Long dealershipId) {
        return dealershipService.findDealershipById(dealershipId);
    }

    @GetMapping
    public List<GetDealershipDTO> findAllDealerships() {
        return dealershipService.findAllDealerships();
    }

    @PostMapping
    public Dealership createDealership(@RequestBody DealershipDTO dealership) {
        return dealershipService.saveDealership(dealership);
    }

    @PutMapping
    public GetDealershipDTO updateDealership(@RequestBody Dealership dealership) {
        return dealershipService.updateDealership(dealership);
    }

    @PatchMapping("addCarToDealership/{dealershipId}")
    public GetDealershipDTO addCarToDealership(@PathVariable Long dealershipId,
                                               @RequestBody Long carId) {
        return dealershipService.addCar(dealershipId, carId);
    }

    @DeleteMapping("/{dealershipId}")
    public void deleteDealership(@PathVariable Long dealershipId) {
        dealershipService.deleteDealership(dealershipId);
    }

    @PatchMapping("/deleteCarFromDs/{dealershipId}")
    public GetDealershipDTO deleteCar(@PathVariable Long dealershipId, @RequestBody Long carId) {
        return dealershipService.deleteCar(dealershipId, carId);

    }
}
