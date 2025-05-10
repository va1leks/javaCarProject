package com.example.project.mappers;

import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Car;
import com.example.project.model.Dealership;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class DealershipMapper {


    public GetDealershipDTO toDto(Dealership dealership)
    {
        if(dealership == null)
            return null;
        return GetDealershipDTO.builder()
                .id(dealership.getId())
                .name(dealership.getName())
                .address(dealership.getAddress())
                .cars(mapCarsToIds(dealership.getCars()))
                .build();
    }

    public List<GetDealershipDTO> toDtos(List<Dealership> dealerships) {
        return dealerships.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<Long> mapCarsToIds(Set<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }
        return cars.stream().map(Car::getId).collect(Collectors.toList());
    }
}