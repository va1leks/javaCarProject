package com.example.demo;

import com.example.project.controllers.CarController;
import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.model.Car;
import com.example.project.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarControllerTest {

    private CarService carService;
    private CarController carController;

    @BeforeEach
    void setUp() {
        carService = mock(CarService.class);
        carController = new CarController(carService);
    }

    @Test
    void findCarByIdShouldReturnCar() {
        Long id = 1L;
        GetCarDTO dto = new GetCarDTO();
        when(carService.findById(id)).thenReturn(dto);

        GetCarDTO result = carController.findCarById(id);

        assertEquals(dto, result);
    }

    @Test
    void findAllCarsShouldReturnList() {
        List<GetCarDTO> cars = List.of(new GetCarDTO());
        when(carService.showCars()).thenReturn(cars);

        List<GetCarDTO> result = carController.findAllCars();

        assertEquals(cars, result);
    }

    @Test
    void saveCarShouldReturnSavedCar() {
        CarDTO dto = new CarDTO();
        GetCarDTO saved = new GetCarDTO();
        when(carService.saveCar(dto)).thenReturn(saved);

        GetCarDTO result = carController.saveCar(dto);

        assertEquals(saved, result);
    }

    @Test
    void updateCarShouldReturnUpdatedCar() {
        Car car = new Car();
        GetCarDTO updated = new GetCarDTO();
        when(carService.updateCar(car)).thenReturn(updated);

        GetCarDTO result = carController.updateCar(car);

        assertEquals(updated, result);
    }

    @Test
    void deleteCarShouldCallService() {
        Long id = 1L;
        doNothing().when(carService).deleteCar(id);

        carController.deleteCar(id);

        verify(carService, times(1)).deleteCar(id);
    }

    @Test
    void patchCarShouldReturnUpdatedCar() {
        Long id = 1L;
        PatchCarDTO patch = new PatchCarDTO();
        GetCarDTO updated = new GetCarDTO();
        when(carService.patchCar(patch, id)).thenReturn(updated);

        GetCarDTO response = carController.patchCar(id, patch);

        assertEquals(updated, response);
    }

    @Test
    void createCarsBulkShouldReturnSavedCars() {
        List<CarDTO> dtos = List.of(new CarDTO());
        List<GetCarDTO> saved = List.of(new GetCarDTO());
        when(carService.saveAllCars(dtos)).thenReturn(saved);

        List<GetCarDTO> response = carController.createCarsBulk(dtos);

        assertEquals(saved, response);
    }
}
