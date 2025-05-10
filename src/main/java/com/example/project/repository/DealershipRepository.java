package com.example.project.repository;

import com.example.project.model.Dealership;
import org.springframework.data.repository.CrudRepository;

public interface DealershipRepository extends
        CrudRepository<Dealership, Long> {

    Dealership findByName(String name);
}
