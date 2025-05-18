package com.example.project.service.impl;

import com.example.project.cache.MyCache;
import com.example.project.constant.Roles;
import com.example.project.dto.create.RegistrationUserDto;
import com.example.project.dto.create.UserDTO;
import com.example.project.dto.get.GetUserDTO;
import com.example.project.dto.patch.UpdateCarDto;
import com.example.project.dto.patch.UpdateUserDto;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ErrorMessages;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.UserMapper;
import com.example.project.model.Car;
import com.example.project.model.User;
import com.example.project.repository.CarRepository;
import com.example.project.repository.ClientRepository;
import com.example.project.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@AllArgsConstructor
@Primary
@Transactional
public class UserServiceImpl implements UserService {
    private final ClientRepository userRepository;
    private final CarRepository carRepository;
    private final UserMapper userMapper;
    private final MyCache<Long, GetUserDTO> clientCache = new MyCache<>(60000, 500);
    private final BCryptPasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    @Transactional
    public GetUserDTO findUserById(Long id) {
        if (clientCache.containsKey(id)) {
            return clientCache.get(id);
        }
        GetUserDTO clientDto = userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id))));
        clientCache.put(id, clientDto);
        return clientDto;
    }

    @Override
    @Transactional
    public List<GetUserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clients not found");
        }
        return userMapper.toDtos(users);
    }

    @Override
    @Transactional
    public User saveUser(UserDTO userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        User user = User.builder().name(userDto.getName())
                .phone(userDto.getPhone()).password(encodedPassword).roles(Collections.singletonList(Roles.ADMIN)).build();
        User savedUser = userRepository.save(user);
        clientCache.put(savedUser.getId(), userMapper.toDto(savedUser));
        return savedUser;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetUserDTO updateUser(UpdateUserDto user, Long id) {
         userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, id)));

        GetUserDTO updatedClient = userMapper.toDto(userRepository.save(userMapper.toUserFronUpdate(user,userRepository)));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id)));
        for (Car car : user.getInterestedCars()) {
            car.getInterestedUsers().remove(user);
        }
        user.getInterestedCars().clear();
        userRepository.delete(user);
        clientCache.getCache().remove(id);
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetUserDTO addInterestedCar(Long carId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId)));
        Car car = carRepository.findById(carId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.CAR_NOT_FOUND, carId)));
        if (user.getInterestedCars().contains(car)) {
            throw new ConflictException("Car already interested");
        }
        car.getInterestedUsers().add(user);
        carRepository.save(car);
        user.getInterestedCars().add(car);
        GetUserDTO updatedClient = userMapper.toDto(userRepository.save(user));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }

    @SneakyThrows
    @Override
    @Transactional
    public GetUserDTO deleteInterestedCar(Long carId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId)));
        Car car = carRepository.findById(carId).orElseThrow(()
                -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, carId)));
        car.getInterestedUsers().remove(user);
        carRepository.save(car);
        user.getInterestedCars().remove(car);
        GetUserDTO updatedClient = userMapper.toDto(userRepository.save(user));
        clientCache.put(updatedClient.getId(), updatedClient);
        return updatedClient;
    }

    @Override
    public GetUserDTO getUserByPhone(String phone) {
        return userMapper.toDto(userRepository.getByPhone(phone));
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByPhone(username).orElseThrow(()->new UsernameNotFoundException(
                String.format(ErrorMessages.USER_NAME_NOT_FOUND, username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                user.getRoles().stream().map(roles -> new SimpleGrantedAuthority(roles.name())).collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User newUser = new User();
        newUser.setPhone(registrationUserDto.getPhone());
        newUser.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        newUser.setName(registrationUserDto.getName());
        newUser.setRoles(List.of(Roles.USER));
        userRepository.save(newUser);
        return newUser;
    }
}
