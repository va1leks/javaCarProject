package com.example.project.dto.get;

import com.example.project.constant.CarStatus;
import com.example.project.constant.EngineType;
import com.example.project.constant.Transmission;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetCarDTO {
    Long id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private int mileage;
    private  String vin;
    private CarStatus status;
    private Transmission transmission;
    private String color;
    private EngineType engineType;
    private List<GetClientDTO> interestedClients;
    private GetDealershipDTO dealershipId;
}
