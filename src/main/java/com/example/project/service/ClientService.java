package com.example.project.service;


import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import java.util.List;

public interface ClientService {

    GetClientDTO findUserById(Long id);

    List<GetClientDTO> findAllUsers();

    Client saveUser(ClientDTO client);

    GetClientDTO updateUser(Client client);

    void deleteUser(Long id);

    GetClientDTO addInterestedCar(Long carId, Long userId);

    GetClientDTO deleteInterestedCar(Long carId, Long userId);
}
