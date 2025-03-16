package com.example.project.service.impl;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.mappers.ClientMapper;
import com.example.project.model.Car;
import com.example.project.model.Client;
import com.example.project.repository.CarRepository;
import com.example.project.repository.ClientRepository;
import com.example.project.service.ClientService;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CarRepository carRepository;
    private final ClientMapper clientMapper;

    @Override
    public GetClientDTO findUserById(Long id) {
        return clientMapper.toDto(clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("client not found")));
    }

    @Override
    @Transactional
    public List<GetClientDTO> findAllUsers() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patients not found");
        }
        return clientMapper.toDtos(clients);
    }

    @Override
    public Client saveUser(ClientDTO clientDto) {
        Client client = Client.builder().name(clientDto.getName())
                .phone(clientDto.getPhone()).build();
        return clientRepository.save(client);
    }

    @Override
    public GetClientDTO updateUser(Client client) {
        Client client1 = clientRepository.findById(client.getId()).orElseThrow(() -> new EntityNotFoundException("client not found"));
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public void deleteUser(Long id) {
        clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("client not found"));
        clientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GetClientDTO addInterestedCar(Long carId, Long userId) {
        Client client = clientRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("client not found"));
        Car car = carRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException("car not found"));
        car.getInterestedClients().add(client);
        carRepository.save(car);
        client.getInterestedCars().add(car);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public GetClientDTO deleteInterestedCar(Long carId, Long userId) {
        Client client = clientRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("client not found"));;
        Car car = carRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException("car not found"));;
        car.getInterestedClients().remove(client);
        carRepository.save(car);
        client.getInterestedCars().remove(car);
        return clientMapper.toDto(clientRepository.save(client));
    }
}
