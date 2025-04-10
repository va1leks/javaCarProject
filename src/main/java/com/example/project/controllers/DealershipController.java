package com.example.project.controllers;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Dealership;
import com.example.project.service.DealershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Dealership Controller", description = "API для управления дилерскими центрами")
public class DealershipController {

    private DealershipService dealershipService;

    @GetMapping("/{dealershipId}")
    @Operation(summary = "Получить дилерский центр по ID",
            description = "Возвращает информацию о дилерском центре по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Дилерский центр найден")
    @ApiResponse(responseCode = "404", description = "Дилерский центр не найден")
    public GetDealershipDTO findDealershipById(@PathVariable Long dealershipId) {
        return dealershipService.findDealershipById(dealershipId);
    }

    @GetMapping
    @Operation(summary = "Получить список всех дилерских центров",
            description = "Возвращает полный список зарегистрированных дилерских центров")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    public List<GetDealershipDTO> findAllDealerships() {
        return dealershipService.findAllDealerships();
    }

    @PostMapping
    @Operation(summary = "Создать дилерский центр",
            description = "Создает новый дилерский центр")
    @ApiResponse(responseCode = "201", description = "Дилерский центр успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public Dealership createDealership(@Valid @RequestBody DealershipDTO dealership) {
        return dealershipService.saveDealership(dealership);
    }

    @PutMapping
    @Operation(summary = "Обновить информацию о дилерском центре",
            description = "Обновляет информацию о существующем дилерском центре")
    @ApiResponse(responseCode = "200", description = "Дилерский центр успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Дилерский центр не найден")
    public GetDealershipDTO updateDealership(@RequestBody Dealership dealership) {
        return dealershipService.updateDealership(dealership);
    }

    @PatchMapping("addCarToDealership/{dealershipId}")
    @Operation(summary = "Добавить машину в дилерский центр",
            description = "Добавляет машину по ID в указанный дилерский центр")
    @ApiResponse(responseCode = "200", description = "Машина успешно добавлена")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Дилерский центр или машина не найдены")
    public GetDealershipDTO addCarToDealership(@PathVariable Long dealershipId,
                                               @RequestBody Long carId) {
        return dealershipService.addCar(dealershipId, carId);
    }

    @DeleteMapping("/{dealershipId}")
    @Operation(summary = "Удалить дилерский центр",
            description = "Удаляет дилерский центр по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Дилерский центр успешно удален")
    @ApiResponse(responseCode = "404", description = "Дилерский центр не найден")
    public void deleteDealership(@PathVariable Long dealershipId) {
        dealershipService.deleteDealership(dealershipId);
    }

    @PatchMapping("/deleteCarFromDs/{dealershipId}")
    @Operation(summary = "Удалить машину из дилерского центра",
            description = "Удаляет указанную машину из дилерского центра")
    @ApiResponse(responseCode = "200", description = "Машина успешно удалена из дилерского центра")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Дилерский центр или машина не найдены")
    public GetDealershipDTO deleteCar(@PathVariable Long dealershipId, @RequestBody Long carId) {
        return dealershipService.deleteCar(dealershipId, carId);

    }
}
