package com.example.project.repository;

import com.example.project.model.Car;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.dealership.id = :dealershipId")
    List<Car> findByDealershipId(@Param("dealershipId") Long dealershipId);

    @Query(
            nativeQuery = true,
            value = "SELECT c.* FROM car c "
                    + "JOIN dealership d ON c.dealership_id = d.id "
                    + "WHERE d.name = :dealershipName"
    )
    List<Car> findByDealershipName(@Param("dealershipName") String dealershipName);

    Car findByVin(String vin);

}
