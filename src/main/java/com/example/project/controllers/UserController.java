package com.example.project.controllers;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("reg")
    public Client createUser(@RequestBody ClientDTO client) {
        return clientService.saveUser(client);
    }


    @PutMapping("update")
    public Client updateUser(@RequestBody Client client) {
        return clientService.updateUser(client);
    }

    @DeleteMapping("deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        clientService.deleteUser(id);
    }

    @PutMapping("addCar/{carId}")
    public Client addInterestedCar(@PathVariable Long carId, @RequestBody Long userId){
        return clientService.addInterestedCar(carId, userId);
    }
}
