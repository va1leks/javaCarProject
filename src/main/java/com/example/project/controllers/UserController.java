package com.example.project.controllers;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import com.example.project.service.ClientService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private ClientService clientService;

    @GetMapping
    public List<GetClientDTO> findAllUsers() {
        return clientService.findAllUsers();
    }

    @GetMapping("{userId}")
    public GetClientDTO findUserById(@PathVariable Long userId) {
        return clientService.findUserById(userId);
    }

    @PostMapping
    public Client createUser(@RequestBody ClientDTO client) {
        return clientService.saveUser(client);
    }


    @PutMapping("update")
    public GetClientDTO updateUser(@RequestBody Client client) {
        return clientService.updateUser(client);
    }

    @DeleteMapping("deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        clientService.deleteUser(id);
    }

    @PatchMapping("addCar/{carId}")
    public GetClientDTO addInterestedCar(@PathVariable Long carId, @RequestBody Long userId) {
        return clientService.addInterestedCar(carId, userId);
    }

    @DeleteMapping("delCar/{carId})")
    public GetClientDTO deleteInterestedCar(@PathVariable Long carId, @RequestBody Long userId) {
        return clientService.deleteInterestedCar(carId, userId);
    }
}
