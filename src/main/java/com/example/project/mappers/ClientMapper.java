package com.example.project.mappers;

import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Car;
import com.example.project.model.Client;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "interestedCars", source = "interestedCars", qualifiedByName = "mapCarsToIds")
    GetClientDTO toDto(Client client);

    List<GetClientDTO> toDtos(List<Client> clients);

    @Named("mapCarsToIds")
    default Set<Long> mapCarsToIds(Set<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return Collections.emptySet();
        }
        return cars.stream().map(Car::getId).collect(Collectors.toSet());


    }
}
