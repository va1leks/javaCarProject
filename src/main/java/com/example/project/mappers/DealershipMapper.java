package com.example.project.mappers;

import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Car;
import com.example.project.model.Dealership;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DealershipMapper {

    @Mapping(target = "cars", source = "cars", qualifiedByName = "mapCarsToIds")
    GetDealershipDTO toDto(Dealership dealership);

    List<GetDealershipDTO> toDtos(List<Dealership> dealerships);

    @Named("mapCarsToIds")
    default List<Long> mapCarsToIds(Set<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }
        return cars.stream().map(Car::getId).collect(Collectors.toList());
    }
}