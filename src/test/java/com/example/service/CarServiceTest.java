package com.example.service;

import com.example.project.constant.CarStatus;
import com.example.project.constant.EngineType;
import com.example.project.constant.Transmission;
import com.example.project.dto.create.CarDTO;
import com.example.project.dto.get.GetCarDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.dto.patch.PatchCarDTO;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.CarMapper;
import com.example.project.model.Car;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private CarMapper carMapper;
    private DealershipRepository dealershipRepository;
    private CarServiceImpl carService;

    private Car car;
    private CarDTO carDTO;
    private GetCarDTO getCarDTO;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carMapper = mock(CarMapper.class);
        dealershipRepository = mock(DealershipRepository.class);
        carService = new CarServiceImpl(carRepository, carMapper, dealershipRepository);

        car = Car.builder()
                .id(1L)
                .brand("Toyota")
                .model("Camry")
                .year(2020)
                .price(20000)
                .mileage(10000)
                .vin("VIN12345678901234")
                .status(CarStatus.AVAILABLE)
                .transmission(Transmission.AUTOMATIC)
                .color("Black")
                .engineType(EngineType.PETROL)
                .build();

        carDTO = new CarDTO();
        carDTO.setBrand("Toyota");
        carDTO.setModel("Camry");
        carDTO.setYear(2020);
        carDTO.setPrice(20000);
        carDTO.setMileage(10000);
        carDTO.setVin("VIN12345678901234");
        carDTO.setStatus(CarStatus.AVAILABLE);
        carDTO.setTransmission(Transmission.AUTOMATIC);
        carDTO.setColor("Black");
        carDTO.setEngineType(EngineType.PETROL);
        carDTO.setDealershipId(1L);

        getCarDTO = GetCarDTO.builder()
                .id(1L)
                .brand("Toyota")
                .model("Camry")
                .year(2020)
                .price(20000)
                .mileage(10000)
                .vin("VIN12345678901234")
                .status(CarStatus.AVAILABLE)
                .transmission(Transmission.AUTOMATIC)
                .color("Black")
                .engineType(EngineType.PETROL)
                .dealershipId(GetDealershipDTO.builder().id(1L).name("TestDealer").build())
                .interestedClients(new ArrayList<>())
                .build();
    }

    @Test
    void testSaveCar() {
        when(carMapper.toEntity(carDTO, dealershipRepository)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(getCarDTO);

        GetCarDTO result = carService.saveCar(carDTO);

        assertEquals(getCarDTO.getId(), result.getId());
        verify(carRepository).save(car);
    }

    @Test
    void testFindById() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(getCarDTO);

        GetCarDTO result = carService.findById(1L);

        assertEquals(getCarDTO.getBrand(), result.getBrand());
    }

    @Test
    void testPatchCar() {
        PatchCarDTO patch = new PatchCarDTO();
        patch.setColor("Red");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(getCarDTO);

        GetCarDTO result = carService.patchCar(patch, 1L);

        assertEquals(getCarDTO.getId(), result.getId());
    }

    @Test
    void testDeleteCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).delete(car);

        assertDoesNotThrow(() -> carService.deleteCar(1L));
        verify(carRepository).delete(car);
    }

    @Test
    void testGetCarsByDealershipName() {
        when(carRepository.findByDealershipName("TestDealer")).thenReturn(List.of(car));
        when(carMapper.toDtos(List.of(car))).thenReturn(List.of(getCarDTO));

        List<GetCarDTO> result = carService.getCarsByDealershipName("TestDealer");

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateCar() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(getCarDTO);

        GetCarDTO result = carService.updateCar(car);

        assertEquals(getCarDTO.getId(), result.getId());
    }

    @Test
    void testShowCars() {
        when(carRepository.findAll()).thenReturn(List.of(car));
        when(carMapper.toDtos(List.of(car))).thenReturn(List.of(getCarDTO));

        List<GetCarDTO> result = carService.showCars();

        assertEquals(1, result.size());
        assertEquals("Toyota", result.getFirst().getBrand());
    }

    @Test
    void testSaveAllCars() {
        when(carMapper.toEntity(carDTO, dealershipRepository)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(getCarDTO);

        List<GetCarDTO> result = carService.saveAllCars(List.of(carDTO));

        assertEquals(1, result.size());
        assertEquals(getCarDTO.getId(), result.getFirst().getId());
    }

    @Test
    void testGetCarsByDealership() {
        when(carRepository.findByDealershipId(1L)).thenReturn(List.of(car));
        when(carMapper.toDtos(List.of(car))).thenReturn(List.of(getCarDTO));

        List<GetCarDTO> result = carService.getCarsByDealership(1L);

        assertEquals(1, result.size());
        assertEquals(getCarDTO.getId(), result.getFirst().getId());
    }

    @Test
    void testFindByIdThrowsException() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.findById(99L));
    }
    @Test
    void testPatchCarWithMultipleFields() {
        PatchCarDTO patch = PatchCarDTO.builder()
                .color("Red")
                .brand("Honda")
                .model("Civic")
                .year(2019)
                .price(15000.0)
                .mileage(5000)
                .vin("NEWVIN1234567890")
                .status(CarStatus.SOLD)
                .transmission(Transmission.MANUAL)
                .engineType(EngineType.DIESEL)
                .dealershipId(1L)
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(dealershipRepository.findById(1L)).thenReturn(Optional.ofNullable(car.getDealership()));
        when(carRepository.save(any())).thenReturn(car);
        when(carMapper.toDto(any())).thenReturn(getCarDTO);

        GetCarDTO result = carService.patchCar(patch, 1L);

        assertEquals(getCarDTO.getId(), result.getId());
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void testPatchCarThrowsException() {
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.patchCar(new PatchCarDTO(), 2L));
    }

    @Test
    void testUpdateCarThrowsException() {
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        Car updatedCar = car;
        updatedCar.setId(2L);

        assertThrows(ResourceNotFoundException.class, () -> carService.updateCar(updatedCar));
    }

    @Test
    void testDeleteCarThrowsException() {
        when(carRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.deleteCar(5L));
    }

    @Test
    void testSaveAllCarsMultipleEntries() {
        CarDTO carDTO2 = new CarDTO();
        carDTO2.setBrand("BMW");
        carDTO2.setModel("X5");
        carDTO2.setYear(2021);
        carDTO2.setPrice(40000);
        carDTO2.setMileage(5000);
        carDTO2.setVin("VIN98765432101234");
        carDTO2.setStatus(CarStatus.AVAILABLE);
        carDTO2.setTransmission(Transmission.AUTOMATIC);
        carDTO2.setColor("White");
        carDTO2.setEngineType(EngineType.DIESEL);
        carDTO2.setDealershipId(1L);

        Car car2 = Car.builder()
                .id(2L)
                .brand("BMW")
                .model("X5")
                .year(2021)
                .price(40000)
                .mileage(5000)
                .vin("VIN98765432101234")
                .status(CarStatus.AVAILABLE)
                .transmission(Transmission.AUTOMATIC)
                .color("White")
                .engineType(EngineType.DIESEL)
                .build();

        GetCarDTO getCarDTO2 = GetCarDTO.builder()
                .id(2L)
                .brand("BMW")
                .model("X5")
                .year(2021)
                .price(40000)
                .mileage(5000)
                .vin("VIN98765432101234")
                .status(CarStatus.AVAILABLE)
                .transmission(Transmission.AUTOMATIC)
                .color("White")
                .engineType(EngineType.DIESEL)
                .dealershipId(GetDealershipDTO.builder().id(1L).name("TestDealer").build())
                .interestedClients(new ArrayList<>())
                .build();

        when(carMapper.toEntity(carDTO, dealershipRepository)).thenReturn(car);
        when(carMapper.toEntity(carDTO2, dealershipRepository)).thenReturn(car2);
        when(carRepository.save(any())).thenReturn(car, car2);
        when(carMapper.toDto(car)).thenReturn(getCarDTO);
        when(carMapper.toDto(car2)).thenReturn(getCarDTO2);

        List<GetCarDTO> result = carService.saveAllCars(List.of(carDTO, carDTO2));

        assertEquals(2, result.size());
    }

}

