package com.example.project.repository;


import com.example.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
}
