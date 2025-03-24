package com.example.project.mappers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Car;
import com.example.project.model.Client;
import com.example.project.model.Dealership;
import com.example.project.repository.DealershipRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;



@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "interestedClients", source = "interestedClients",
            qualifiedByName = "mapClientsToDto")
    @Mapping(target = "dealershipId", source = "dealership", qualifiedByName = "mapDealershipToDto")
    GetCarDTO toDto(Car car);

    List<GetCarDTO> toDtos(List<Car> cars);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealership", source = "dealershipId",
            qualifiedByName = "mapDealershipIdToDealership")
    Car toEntity(CarDTO dto, @Context DealershipRepository dealershipRepository);

    @Named("mapDealershipIdToDealership")
    default Dealership mapDealershipIdToDealership(Long dealershipId,
              @Context DealershipRepository dealershipRepository) {
        return dealershipId == null ? null : dealershipRepository.findById(dealershipId)
                .orElseThrow(() -> new RuntimeException(
                        "Dealership not found with id: " + dealershipId));
    }

    @Named("mapDealershipToDto")
    default GetDealershipDTO mapDealershipToDto(Dealership dealership) {
        if (dealership == null) {
            return null;
        }

        List<Long> carIds = dealership.getCars().stream()
                .map(Car::getId)
                .collect(Collectors.toList());

        return GetDealershipDTO.builder()
                .id(dealership.getId())
                .name(dealership.getName())
                .address(dealership.getAddress())
                .cars(carIds)
                .build();
    }

    @Named("mapClientsToDto")
    default List<GetClientDTO> mapClientsToDto(List<Client> clients) {
        if (clients == null || clients.isEmpty()) {
            return Collections.emptyList();
        }

        return clients.stream().map(client -> GetClientDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .interestedCars(client.getInterestedCars().stream()
                        .map(Car::getId)
                        .collect(Collectors.toSet()))
                .build()).collect(Collectors.toList());
    }
}


