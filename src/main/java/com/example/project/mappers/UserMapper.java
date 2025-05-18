package com.example.project.mappers;

import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.get.GetUserDTO;
import com.example.project.dto.patch.UpdateUserDto;
import com.example.project.model.Car;
import com.example.project.model.User;
import com.example.project.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



@AllArgsConstructor
@Component
public class UserMapper {

    private CarMapper carMapper;

    public Optional<GetUserDTO> toDto(Optional<User> user)
    {
        return Optional.ofNullable(GetUserDTO.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .phone(user.get().getPhone())
                .interestedCars(mapCarsToGetCars(user.get().getInterestedCars()))
                .roles(user.get().getRoles())
                .build());
    }

    public GetUserDTO toDto(User user)
    {
        return GetUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .interestedCars(mapCarsToGetCars(user.getInterestedCars()))
                .roles(user.getRoles())
                .build();
    }

    public List<GetUserDTO> toDtos(List<User> users) {
        if (users == null) {
            return Collections.emptyList(); // Возвращаем пустой список, если входной список пуст
        }

        return users.stream()
                .map(this::toDto)  // Преобразуем каждый объект User в GetUserDTO
                .collect(Collectors.toList());  // Собираем в List<GetUserDTO>
    }


    public Set<GetCarDTO> mapCarsToGetCars(Set<Car> cars) {
        if (cars == null) {
            return Collections.emptySet(); // Возвращаем пустое множество, если входной набор пуст
        }

        return cars.stream()
                .map(carMapper::toGetDto)  // Используем carMapper для преобразования каждого Car в GetCarDTO
                .collect(Collectors.toSet());  // Собираем в Set<GetCarDTO>
    }

    public User toUserFronUpdate(UpdateUserDto updateUserDto, ClientRepository clientRepository) {
        return User.builder()
                .roles(updateUserDto.getRoles())
                .name(updateUserDto.getName())
                .id(clientRepository.findByPhone(updateUserDto.getPhone()).get().getId())
                .password(clientRepository.findByPhone(updateUserDto.getPhone()).get().getPassword())
                .interestedCars(clientRepository.findByPhone(updateUserDto.getPhone()).get().getInterestedCars())
                .phone(updateUserDto.getPhone()).build();
    }

}
