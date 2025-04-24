package com.example.project.service.impl;

import com.example.project.cache.MyCache;
import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ErrorMessages;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.ClientMapper;
import com.example.project.model.Car;
import com.example.project.model.Client;
import com.example.project.repository.CarRepository;
import com.example.project.repository.ClientRepository;
import com.example.project.service.ClientService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
    private final MyCache<Long, GetClientDTO> clientCache = new MyCache<>(60000, 500);

    @SneakyThrows
    @Override
    @Transactional
    public GetClientDTO findUserById(Long id) {
        if (clientCache.containsKey(id)) {
            return clientCache.get(id);
        }
        GetClientDTO clientDto = clientMapper.toDto(clientRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id))));
        clientCache.put(id, clientDto);
        return clientDto;
    }

    @Override
    @Transactional
    public List<GetClientDTO> findAllUsers() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clients not found");
        }
        return clientMapper.toDtos(clients);
    }

    @Override
    @Transactional
    public Client saveUser(ClientDTO clientDto) {
        Client client = Client.builder().name(clientDto.getName())
                .phone(clientDto.getPhone()).build();
        Client savedClient = clientRepository.save(client);
        clientCache.put(savedClient.getId(), clientMapper.toDto(savedClient));
        return savedClient;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetClientDTO updateUser(Client client) {
        clientRepository.findById(client.getId()).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, client.getId())));
        GetClientDTO updatedClient = clientMapper.toDto(clientRepository.save(client));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteUser(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id)));
        for (Car car : client.getInterestedCars()) {
            car.getInterestedClients().remove(client);
        }
        client.getInterestedCars().clear();
        clientRepository.delete(client);
        clientCache.getCache().remove(id);
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetClientDTO addInterestedCar(Long carId, Long userId) {
        Client client = clientRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId)));
        Car car = carRepository.findById(carId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.CAR_NOT_FOUND, carId)));
        if (client.getInterestedCars().contains(car)) {
            throw new ConflictException("Car already interested");
        }
        car.getInterestedClients().add(client);
        carRepository.save(car);
        client.getInterestedCars().add(car);
        GetClientDTO updatedClient = clientMapper.toDto(clientRepository.save(client));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetClientDTO deleteInterestedCar(Long carId, Long userId) {
        Client client = clientRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId)));
        Car car = carRepository.findById(carId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, carId)));
        car.getInterestedClients().remove(client);
        carRepository.save(car);
        client.getInterestedCars().remove(car);
        GetClientDTO updatedClient = clientMapper.toDto(clientRepository.save(client));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }
}
