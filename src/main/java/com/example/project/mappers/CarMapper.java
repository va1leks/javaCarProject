package com.example.project.mappers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.model.Car;
import com.example.project.model.Client;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "interestedClients", source = "interestedClients", qualifiedByName = "mapClientsToIds")
    @Mapping(target = "dealershipId", source = "dealership.id")
    GetCarDTO toDto(Car car);

    List<GetCarDTO> toDtos(List<Car> cars);

    @Mapping(target = "id", ignore = true)
    Car toEntity(CarDTO dto);

    @Named("mapClientsToIds")
    default List<Long> mapClientsToIds(List<Client> clients) {
        if (clients == null || clients.isEmpty()) {
            return Collections.emptyList();
        }
        return clients.stream().map(Client::getId).collect(Collectors.toList());
    }

    List<Car> toEntities(List<CarDTO> carDTOs);
}
