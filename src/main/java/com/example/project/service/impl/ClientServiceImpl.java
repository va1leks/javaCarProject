package com.example.project.service.impl;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.mappers.ClientMapper;
import com.example.project.model.Car;
import com.example.project.model.Client;
import com.example.project.repository.CarRepository;
import com.example.project.repository.ClientRepository;
import com.example.project.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


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
      return clientMapper.toDto(clientRepository.findById(id).orElse(null));
   }

   @Override
   @Transactional
   public List<GetClientDTO> findAllUsers() {
      return clientMapper.toDtos(clientRepository.findAll());
   }

   @Override
   public Client saveUser(Client client) {
      return clientRepository.save(client);
   }

   @Override
   public Client saveUser(ClientDTO clientDTO) {
      Client client = Client.builder().name(clientDTO.getName()).phone(clientDTO.getPhone()).build();
      return clientRepository.save(client);
   }

   @Override
   public Client updateUser(Client client) {
      return clientRepository.save(client);
   }

   @Override
   public void deleteUser(Long id) {
      clientRepository.deleteById(id);
   }

   @Override
   public Client addInterestedCar(Long carId, Long userId) {
      Client client = clientRepository.findById(userId).orElseThrow();
      Car car = carRepository.findById(carId).orElseThrow();
      car.getInterestedClients().add(client);
      carRepository.save(car);
      client.getInterestedCars().add(car);
      return clientRepository.save(client);
   }
}
