package com.example.project.service;


import com.example.project.dto.create.UserDTO;
import com.example.project.dto.get.GetUserDTO;
import com.example.project.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    GetUserDTO findUserById(Long id);

    List<GetUserDTO> findAllUsers();

    User saveUser(UserDTO client);

    GetUserDTO updateUser(User user);

    void deleteUser(Long id);

    GetUserDTO addInterestedCar(Long carId, Long userId);

    GetUserDTO deleteInterestedCar(Long carId, Long userId);

    Optional<User> findUserByPhone(String phone);
}
