package com.example.demo;

import com.example.project.controllers.UserController;
import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.model.Client;
import com.example.project.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private static final String CLIENT_PHONE_NUMBER = "123456789";
    private static final String CLIENT_NAME = "John Doe";

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

        clientDto = new ClientDTO(CLIENT_NAME, CLIENT_PHONE_NUMBER);

        client = new Client();
        client.setId(1L);
        client.setName(CLIENT_NAME);
        client.setPhone(CLIENT_PHONE_NUMBER);

        Set<Long> interestedCars = new HashSet<>();
        interestedCars.add(1L);
        getClientDto = GetClientDTO.builder()
                .id(1L)
                .name(CLIENT_NAME)
                .phone(CLIENT_PHONE_NUMBER)
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
        assertEquals(CLIENT_NAME, response.getFirst().getName());
        verify(clientService, times(1)).findAllUsers();
    }

    @Test
    void findUserByIdShouldReturnUserWhenUserExists() {
        when(clientService.findUserById(1L)).thenReturn(getClientDto);

        GetClientDTO response = userController.findUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(CLIENT_NAME, response.getName());
        assertEquals(CLIENT_PHONE_NUMBER, response.getPhone());
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
        assertEquals(CLIENT_NAME, response.getName());
        assertEquals(CLIENT_PHONE_NUMBER, response.getPhone());
        verify(clientService, times(1)).saveUser(clientDto);
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        when(clientService.updateUser(client)).thenReturn(getClientDto);

        GetClientDTO response = userController.updateUser(client);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(CLIENT_NAME, response.getName());
        assertEquals(CLIENT_PHONE_NUMBER, response.getPhone());
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
