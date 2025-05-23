package com.example.project.controllers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.service.CarService;
import com.example.project.service.impl.CarServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
@Tag(name = "Car Controller", description = "API для управления машинами")
@Slf4j
public class CarController {

    private final CarServiceImpl carServiceImpl;
    private CarService carService;

    @GetMapping("{id}")
    @Operation(summary = "Получить машину по ID",
            description = "Возвращает информацию о машине по ее идентификатору")
    @ApiResponse(responseCode = "200", description = "Машина найдена")
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    public GetCarDTO findCarById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Получить все машины", description = "Возвращает список всех машин")
    @ApiResponse(responseCode = "200", description = "Список машин успешно получен")
    public List<GetCarDTO> findAllCars() {
        return carService.showCars();
    }

    @PostMapping
    @Operation(summary = "Создать профиль машины", description = "Создает новый профиль машины")
    @ApiResponse(responseCode = "201", description = "Профиль машины успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public GetCarDTO saveCar(@Valid @RequestBody CarDTO car) {
        return carService.saveCar(car);
    }




    @PutMapping("updateCar/{id}")
    @Operation(summary = "Обновить информацию о машине",
            description = "Полностью обновляет информацию о машине")
    @ApiResponse(responseCode = "200", description = "Машина успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    public GetCarDTO updateCar(@Valid @RequestBody UpdateCarDto car, @PathVariable Long id) {
        return carServiceImpl.updateCar(car,id);
    }


    @DeleteMapping("/{carId}")
    @Operation(summary = "Удалить машину",
            description = "Удаляет машину по ID")
    @ApiResponse(responseCode = "204", description = "Машина успешно удалена")
    @ApiResponse(responseCode = "404", description = "Машина не найдена")
    public ResponseEntity<String> deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok("Машина с ID " + carId + " успешно удалена");
    }



    @GetMapping("/by-dealership/{dealershipId}")
    @Operation(summary = "Получить машины по ID дилерского центра",
            description = "Возвращает список машин, принадлежащих указанному дилерскому центру")
    @ApiResponse(responseCode = "200", description = "Список машин успешно получен")
    @ApiResponse(responseCode = "404", description = "Дилерский центр не найден")
    public List<GetCarDTO> getCarsByDealership(@PathVariable Long dealershipId) {
        return carService.getCarsByDealership(dealershipId);
    }

    @GetMapping("/by-dealership-name/{dealershipName}")
    @Operation(summary = "Получить машины по названию дилерского центра",
            description = "Возвращает список машин, принадлежащих указанному дилерскому центру")
    @ApiResponse(responseCode = "200", description = "Список машин успешно получен")
    @ApiResponse(responseCode = "404", description = "Дилерский центр не найден")
    public List<GetCarDTO> getCarsByName(@PathVariable String dealershipName) {
        return carService.getCarsByDealershipName(dealershipName);
    }

    @PostMapping("/bulk")
    public List<GetCarDTO> createCarsBulk(@RequestBody List<CarDTO> carDtos) {
        return carService.saveAllCars(carDtos);
    }
}
