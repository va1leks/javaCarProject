package com.example.project.controllers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.project.dto.create.DealershipDTO;
import com.example.project.dto.get.GetDealershipDTO;
import com.example.project.model.Dealership;
import com.example.project.service.DealershipService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DealershipControllerTest {

    private static final String DEALERSHIP_NAME = "Test Dealership";
    private static final String DEALERSHIP_ADDRESS = "Test Location";


    @Mock
    private DealershipService dealershipService;

    @InjectMocks
    private DealershipController dealershipController;

    private DealershipDTO dealershipDto;
    private Dealership dealership;
    private GetDealershipDTO getDealershipDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dealershipDto = new DealershipDTO();
        dealershipDto.setName(DEALERSHIP_NAME);
        dealershipDto.setAddress( DEALERSHIP_ADDRESS);

        dealership = new Dealership();
        dealership.setId(1L);
        dealership.setName(DEALERSHIP_NAME);
        dealership.setAddress( DEALERSHIP_ADDRESS);

        getDealershipDto = new GetDealershipDTO();
        getDealershipDto.setId(1L);
        getDealershipDto.setName(DEALERSHIP_NAME);
        getDealershipDto.setAddress( DEALERSHIP_ADDRESS);
    }

    @Test
    void findDealershipByIdShouldReturnDealershipWhenDealershipExists() {
        when(dealershipService.findDealershipById(1L)).thenReturn(getDealershipDto);

        GetDealershipDTO response = dealershipController.findDealershipById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(DEALERSHIP_NAME, response.getName());
        assertEquals( DEALERSHIP_ADDRESS, response.getAddress());
        verify(dealershipService, times(1)).findDealershipById(1L);
    }

    @Test
    void findDealershipByIdShouldReturnNullWhenDealershipDoesNotExist() {
        when(dealershipService.findDealershipById(1L)).thenReturn(null);

        GetDealershipDTO response = dealershipController.findDealershipById(1L);

        assertNull(response);
        verify(dealershipService, times(1)).findDealershipById(1L);
    }

    @Test
    void findAllDealershipsShouldReturnListOfDealerships() {
        when(dealershipService.findAllDealerships())
                .thenReturn(Collections.singletonList(getDealershipDto));

        List<GetDealershipDTO> response = dealershipController.findAllDealerships();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        verify(dealershipService, times(1)).findAllDealerships();
    }

    @Test
    void createDealershipShouldReturnCreatedDealership() {
        when(dealershipService.saveDealership(dealershipDto)).thenReturn(dealership);

        Dealership response = dealershipController.createDealership(dealershipDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(DEALERSHIP_NAME, response.getName());
        assertEquals( DEALERSHIP_ADDRESS, response.getAddress());
        verify(dealershipService, times(1)).saveDealership(dealershipDto);
    }

    @Test
    void updateDealershipShouldReturnUpdatedDealership() {
        when(dealershipService.updateDealership(dealership)).thenReturn(getDealershipDto);

        GetDealershipDTO response = dealershipController.updateDealership(dealership);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(DEALERSHIP_NAME, response.getName());
        assertEquals( DEALERSHIP_ADDRESS, response.getAddress());
        verify(dealershipService, times(1)).updateDealership(dealership);
    }

    @Test
    void deleteDealershipShouldReturnVoidWhenDealershipDeleted() {
        doNothing().when(dealershipService).deleteDealership(1L);

        dealershipController.deleteDealership(1L);

        verify(dealershipService, times(1)).deleteDealership(1L);
    }

    @Test
    void addCarToDealershipShouldReturnUpdatedDealership() {
        when(dealershipService.addCar(1L, 1L)).thenReturn(getDealershipDto);

        GetDealershipDTO response = dealershipController.addCarToDealership(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(dealershipService, times(1)).addCar(1L, 1L);
    }

    @Test
    void deleteCarShouldReturnUpdatedDealership() {
        when(dealershipService.deleteCar(1L, 1L)).thenReturn(getDealershipDto);

        GetDealershipDTO response = dealershipController.deleteCar(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(dealershipService, times(1)).deleteCar(1L, 1L);
    }
}
