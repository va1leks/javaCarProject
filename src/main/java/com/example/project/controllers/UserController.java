package com.example.project.controllers;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import com.example.project.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {
    private ClientService clientService;

    @GetMapping
    @Operation(summary = "Получить список всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public List<GetClientDTO> findAllUsers() {
        return clientService.findAllUsers();
    }

    @GetMapping("{userId}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает информацию о конкретном пользователе по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public GetClientDTO findUserById(@PathVariable Long userId) {
        return clientService.findUserById(userId);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя",
            description = "Создает нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public Client createUser(@RequestBody ClientDTO client) {
        return clientService.saveUser(client);
    }


    @PutMapping("update")
    @Operation(summary = "Обновить информацию о пользователе",
            description = "Обновляет информацию о существующем пользователе")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public GetClientDTO updateUser(@RequestBody Client client) {
        return clientService.updateUser(client);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя",
            description = "Удаляет пользователя по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public void deleteUser(@PathVariable Long id) {
        clientService.deleteUser(id);
    }

    @PatchMapping("addCar/{carId}")
    @Operation(summary = "Добавить машину в список интересов пользователя",
            description = "Добавляет машину в список интересов пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Машина успешно добавлена в список интересов")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь или машина не найдены")
    public GetClientDTO addInterestedCar(@PathVariable Long carId, @RequestBody Long userId) {
        return clientService.addInterestedCar(carId, userId);
    }

    @DeleteMapping("delCar/{carId}")
    @Operation(summary = "Удалить машину из списка интересов пользователя",
            description = "Удаляет указанную машину из списка интересов пользователя")
    @ApiResponse(responseCode = "200", description = "Машина успешно удалена из списка интересов")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь или машина не найдены")
    public GetClientDTO deleteInterestedCar(@PathVariable Long carId, @RequestBody Long userId) {
        return clientService.deleteInterestedCar(carId, userId);
    }
}
