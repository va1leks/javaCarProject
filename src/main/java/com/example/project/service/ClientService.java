package com.example.project.service;


import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientService {

    GetClientDTO findUserById(Long id);

    List<GetClientDTO> findAllUsers();

    @Transactional
    Client saveUser(Client client);
    @Transactional
    Client saveUser(ClientDTO client);
    @Transactional
    Client updateUser(Client client);
    @Transactional
    void deleteUser(Long id);

    Client addInterestedCar(Long carId, Long userId);
}
