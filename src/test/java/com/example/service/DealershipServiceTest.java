package com.example.service;

import com.example.project.cache.MyCache;
import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.DealershipMapper;
import com.example.project.model.Car;
import com.example.project.model.Dealership;
import com.example.project.repository.CarRepository;
import com.example.project.repository.DealershipRepository;
import com.example.project.service.impl.DealershipServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DealershipServiceTest {

    private DealershipRepository dealershipRepository;
    private CarRepository carRepository;
    private DealershipMapper dealershipMapper;
    private DealershipServiceImpl dealershipService;

    private Dealership dealership;
    private DealershipDTO dealershipDTO;
    private GetDealershipDTO getDealershipDTO;

    @BeforeEach
    void setUp() {
        dealershipRepository = mock(DealershipRepository.class);
        carRepository = mock(CarRepository.class);
        dealershipMapper = mock(DealershipMapper.class);
        dealershipService = new DealershipServiceImpl(dealershipRepository, carRepository, dealershipMapper);

        dealership = Dealership.builder()
                .id(1L)
                .name("TestDealer")
                .address("Main St")
                .cars(new HashSet<>())
                .build();

        dealershipDTO = new DealershipDTO();
        dealershipDTO.setName("TestDealer");
        dealershipDTO.setAddress("Main St");

        getDealershipDTO = GetDealershipDTO.builder()
                .id(1L)
                .name("TestDealer")
                .address("Main St")
                .cars(new ArrayList<>())
                .build();
    }


    @Test
    void testFindDealershipByIdNotCached() {
        when(dealershipRepository.findById(1L)).thenReturn(Optional.of(dealership));
        when(dealershipMapper.toDto(dealership)).thenReturn(getDealershipDTO);

        GetDealershipDTO result = dealershipService.findDealershipById(1L);

        assertEquals(getDealershipDTO.getId(), result.getId());
    }

    @Test
    void testFindDealershipByIdNotFound() {
        when(dealershipRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealershipService.findDealershipById(99L));
    }

    @Test
    void testFindAllDealerships() {
        when(dealershipRepository.findAll()).thenReturn(List.of(dealership));
        when(dealershipMapper.toDtos(List.of(dealership))).thenReturn(List.of(getDealershipDTO));

        List<GetDealershipDTO> result = dealershipService.findAllDealerships();

        assertEquals(1, result.size());
    }

    @Test
    void testFindAllDealershipsEmpty() {
        when(dealershipRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> dealershipService.findAllDealerships());
    }

    @Test
    void testSaveDealership() {
        when(dealershipRepository.save(any(Dealership.class))).thenReturn(dealership);
        when(dealershipMapper.toDto(dealership)).thenReturn(getDealershipDTO);

        Dealership result = dealershipService.saveDealership(dealershipDTO);

        assertEquals(dealership.getName(), result.getName());
    }

    @Test
    void testUpdateDealership() {
        when(dealershipRepository.save(dealership)).thenReturn(dealership);
        when(dealershipMapper.toDto(dealership)).thenReturn(getDealershipDTO);

        GetDealershipDTO result = dealershipService.updateDealership(dealership);

        assertEquals(getDealershipDTO.getId(), result.getId());
    }

    @Test
    void testAddCar() {
        Car car = Car.builder().id(2L).build();
        when(dealershipRepository.findById(1L)).thenReturn(Optional.of(dealership));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(dealershipRepository.save(dealership)).thenReturn(dealership);
        when(dealershipMapper.toDto(dealership)).thenReturn(getDealershipDTO);

        GetDealershipDTO result = dealershipService.addCar(1L, 2L);

        assertEquals(getDealershipDTO.getId(), result.getId());
        assertTrue(dealership.getCars().contains(car));
    }

    @Test
    void testAddCar_DealershipNotFound() {
        when(dealershipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealershipService.addCar(1L, 2L));
    }

    @Test
    void testAddCarCarNotFound() {
        when(dealershipRepository.findById(1L)).thenReturn(Optional.of(dealership));
        when(carRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealershipService.addCar(1L, 2L));
    }

    @Test
    void testDeleteDealership() {
        doNothing().when(dealershipRepository).deleteById(1L);

        dealershipService.deleteDealership(1L);

        verify(dealershipRepository).deleteById(1L);
    }

    @Test
    void testDeleteCar() {
        Car car = Car.builder().id(2L).build();
        dealership.getCars().add(car);

        when(dealershipRepository.findById(1L)).thenReturn(Optional.of(dealership));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(dealershipRepository.save(dealership)).thenReturn(dealership);
        when(dealershipMapper.toDto(dealership)).thenReturn(getDealershipDTO);

        GetDealershipDTO result = dealershipService.deleteCar(1L, 2L);

        assertEquals(getDealershipDTO.getId(), result.getId());
    }

    @Test

    void testFindDealershipById_CacheHit() throws Exception {
        GetDealershipDTO cachedDealership = GetDealershipDTO.builder()
                .id(1L)
                .name("TestDealer")
                .address("Main St")
                .build();

        Field field = DealershipServiceImpl.class.getDeclaredField("dealershipCache");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        MyCache<Long, GetDealershipDTO> cache = (MyCache<Long, GetDealershipDTO>) field.get(dealershipService);
        cache.put(1L, cachedDealership);

        GetDealershipDTO result = dealershipService.findDealershipById(1L);

        assertEquals(cachedDealership.getId(), result.getId());
        assertEquals(cachedDealership.getName(), result.getName());
    }
    @Test
    void testFindDealershipById_NotFound() {
        when(dealershipRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealershipService.findDealershipById(99L));
    }
}
