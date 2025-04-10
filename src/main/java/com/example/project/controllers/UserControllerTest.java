package com.example.project.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import com.example.project.service.ClientService;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private UserController userController;

    private ClientDTO clientDto;
    private Client client;
    private GetClientDTO getClientDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clientDto = new ClientDTO("John Doe", "1234567890");

        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setPhone("1234567890");

        Set<Long> interestedCars = new HashSet<>();
        interestedCars.add(1L);
        getClientDto = GetClientDTO.builder()
                .id(1L)
                .name("John Doe")
                .phone("1234567890")
                .interestedCars(interestedCars)
                .build();
    }

    @Test
    void findAllUsersShouldReturnListOfUsers() {
        when(clientService.findAllUsers()).thenReturn(Collections.singletonList(getClientDto));

        List<GetClientDTO> response = userController.findAllUsers();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getName());
        verify(clientService, times(1)).findAllUsers();
    }

    @Test
    void findUserByIdShouldReturnUserWhenUserExists() {
        when(clientService.findUserById(1L)).thenReturn(getClientDto);

        GetClientDTO response = userController.findUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("1234567890", response.getPhone());
        verify(clientService, times(1)).findUserById(1L);
    }

    @Test
    void findUserByIdShouldReturnNullWhenUserDoesNotExist() {
        when(clientService.findUserById(1L)).thenReturn(null);

        GetClientDTO response = userController.findUserById(1L);

        assertNull(response);
        verify(clientService, times(1)).findUserById(1L);
    }

    @Test
    void createUserShouldReturnCreatedUser() {
        when(clientService.saveUser(clientDto)).thenReturn(client);

        Client response = userController.createUser(clientDto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("1234567890", response.getPhone());
        verify(clientService, times(1)).saveUser(clientDto);
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        when(clientService.updateUser(client)).thenReturn(getClientDto);

        GetClientDTO response = userController.updateUser(client);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("1234567890", response.getPhone());
        verify(clientService, times(1)).updateUser(client);
    }

    @Test
    void deleteUserShouldReturnVoidWhenUserDeleted() {
        doNothing().when(clientService).deleteUser(1L);

        userController.deleteUser(1L);

        verify(clientService, times(1)).deleteUser(1L);
    }

    @Test
    void addInterestedCarShouldReturnUpdatedUser() {
        when(clientService.addInterestedCar(1L, 1L)).thenReturn(getClientDto);

        GetClientDTO response = userController.addInterestedCar(1L, 1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertTrue(response.getInterestedCars().contains(1L));
        verify(clientService, times(1)).addInterestedCar(1L, 1L);
    }

}
