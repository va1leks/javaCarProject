package com.example.project.controllers;

import com.example.project.dto.create.UserDTO;
import com.example.project.dto.get.GetUserDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.dto.patch.UpdateUserDto;
import com.example.project.mappers.UserMapper;
import com.example.project.model.User;
import com.example.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserMapper userMapper;
    private UserService userService;

    @GetMapping
    @Operation(summary = "Получить список всех пользователей",
            description = "Возвращает список всех зарегистрированных пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    public List<GetUserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("{userId}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает информацию о конкретном пользователе по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public GetUserDTO findUserById(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }

    @PutMapping("{id}")
    @Operation(summary = "Обновить информацию о пользователе",
            description = "Обновляет информацию о существующем пользователе по его ID")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public GetUserDTO updateUser(@PathVariable Long id, @RequestBody UpdateUserDto user) {
        return userService.updateUser(user,id);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя",
            description = "Удаляет пользователя по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("addCar/{carId}")
    @Operation(summary = "Добавить машину в список интересов пользователя",
            description = "Добавляет машину в список интересов пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Машина успешно добавлена в список интересов")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь или машина не найдены")
    public GetUserDTO addInterestedCar(@PathVariable Long carId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.addInterestedCar(carId, userService.findUserByPhone(username).get().getId());
    }

    @DeleteMapping("delCar/{carId}")
    @Operation(summary = "Удалить машину из списка интересов пользователя",
            description = "Удаляет указанную машину из списка интересов пользователя")
    @ApiResponse(responseCode = "200", description = "Машина успешно удалена из списка интересов")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь или машина не найдены")
    public GetUserDTO deleteInterestedCar(@PathVariable Long carId,Principal principal) {
        return userService.deleteInterestedCar(carId, userService.findUserByPhone(principal.getName()).get().getId());
    }

    @GetMapping("/profile")
    public GetUserDTO getUserInfo(Principal principal) {
        return userService.getUserByPhone(principal.getName());
    }

    @GetMapping("/admin")
    public String getUserIasd(Principal principal) {
        return principal.toString()+"admin!!!!!!!!!!!!!!!!!!!!";
    }
}
