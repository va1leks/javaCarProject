package com.example.project.mappers;

import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
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
            qualifiedByName = "mapClientsToIds")
    @Mapping(target = "dealershipId", source = "dealership.id")
    GetCarDTO toDto(Car car);

    List<GetCarDTO> toDtos(List<Car> cars);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealership", source = "dealershipId",
            qualifiedByName = "mapDealershipIdToDealership")
    Car toEntity(CarDTO dto, @Context DealershipRepository dealershipRepository);

    @Named("mapDealershipIdToDealership")
    default Dealership mapDealershipIdToDealership(Long dealershipId,
            @Context DealershipRepository dealershipRepository) {
        if (dealershipId == null) {
            return null;
        }
        return dealershipRepository.findById(dealershipId)
                .orElseThrow(() -> new
                        RuntimeException("Dealership not found with id: " + dealershipId));
    }


    @Named("mapClientsToIds")
    default List<Long> mapClientsToIds(List<Client> clients) {
        if (clients == null || clients.isEmpty()) {
            return Collections.emptyList();
        }
        return clients.stream().map(Client::getId).collect(Collectors.toList());
    }

}
