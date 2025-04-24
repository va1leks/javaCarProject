package com.example.service;

import com.example.project.cache.MyCache;
import com.example.project.dto.create.ClientDTO;
import com.example.project.dto.get.GetClientDTO;
import com.example.project.exception.ConflictException;
import com.example.project.exception.ResourceNotFoundException;
import com.example.project.mappers.ClientMapper;
import com.example.project.model.Car;
import com.example.project.model.Client;
import com.example.project.repository.CarRepository;
import com.example.project.repository.ClientRepository;

import java.lang.reflect.Field;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.project.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientServiceImpl(clientRepository, carRepository, clientMapper);
    }



    @Test
    void testFindUserById_WhenNotCached() {
        Long id = 1L;
        Client client = Client.builder().id(id).name("John").build();
        GetClientDTO dto = GetClientDTO.builder().id(id).name("John").build();

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(dto);

        GetClientDTO result = clientService.findUserById(id);
        assertEquals(dto, result);
    }

    @Test
    void testFindUserById_ThrowsIfNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.findUserById(99L));
    }

    @Test
    void testFindAllUsers() {
        List<Client> clients = List.of(new Client());
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toDtos(clients)).thenReturn(List.of(new GetClientDTO()));
        assertFalse(clientService.findAllUsers().isEmpty());
    }

    @Test
    void testFindAllUsers_ThrowsIfEmpty() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ResponseStatusException.class, () -> clientService.findAllUsers());
    }

    @Test
    void testSaveUser() {
        ClientDTO dto = new ClientDTO("John", "123456");
        Client saved = Client.builder().id(1L).name("John").phone("123456").build();
        GetClientDTO resultDto = GetClientDTO.builder().id(1L).name("John").phone("123456").build();

        when(clientRepository.save(any())).thenReturn(saved);
        when(clientMapper.toDto(saved)).thenReturn(resultDto);

        Client result = clientService.saveUser(dto);
        assertEquals(saved.getName(), result.getName());
    }

    @Test
    void testUpdateUser() {
        Client client = Client.builder().id(1L).name("Updated").build();
        GetClientDTO dto = GetClientDTO.builder().id(1L).name("Updated").build();

        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(dto);

        GetClientDTO result = clientService.updateUser(client);
        assertEquals(dto, result);
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        Client client = Client.builder().id(id).interestedCars(new HashSet<>()).build();

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        assertDoesNotThrow(() -> clientService.deleteUser(id));
        verify(clientRepository).delete(client);
    }

    @Test
    void testAddInterestedCar() {
        Long clientId = 1L, carId = 2L;

        // Создание клиента и машины
        Client client = Client.builder()
                .id(clientId)
                .interestedCars(new HashSet<>())
                .build();
        Car car = Car.builder()
                .id(carId)
                .interestedClients(new ArrayList<>())
                .build();

        // Настройка моков
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(clientRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clientMapper.toDto(any())).thenReturn(GetClientDTO.builder().id(clientId).build());

        // Вызов метода
        GetClientDTO result = clientService.addInterestedCar(carId, clientId);

        // Проверка
        assertEquals(clientId, result.getId());
    }

    @Test
    void testAddInterestedCarThrowsConflict() {
        Long clientId = 1L, carId = 2L;

        Car car = Car.builder()
                .id(carId)
                .interestedClients(new ArrayList<>())
                .build();

        Client client = Client.builder()
                .id(clientId)
                .interestedCars(new HashSet<>(Set.of(car)))
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Проверка, что выбрасывается ConflictException
        assertThrows(ConflictException.class, () -> clientService.addInterestedCar(carId, clientId));
    }

    @Test
    void testDeleteInterestedCar() {
        Long clientId = 1L, carId = 2L;

        Client client = Client.builder()
                .id(clientId)
                .interestedCars(new HashSet<>())
                .build();
        Car car = Car.builder()
                .id(carId)
                .interestedClients(new ArrayList<>())
                .build();

        client.getInterestedCars().add(car);
        car.getInterestedClients().add(client);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(GetClientDTO.builder().id(clientId).build());

        GetClientDTO result = clientService.deleteInterestedCar(carId, clientId);
        assertEquals(clientId, result.getId());
    }
    @Test
    void testAddInterestedCarThrowsCarNotFound() {
        Long clientId = 1L, carId = 2L;

        Client client = Client.builder()
                .id(clientId)
                .interestedCars(new HashSet<>())
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.addInterestedCar(carId, clientId));
    }

    @Test
    void testDeleteInterestedCarThrowsClientNotFound() {
        Long clientId = 1L, carId = 2L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteInterestedCar(carId, clientId));
    }
    @Test
    void testDeleteInterestedCarThrowsCarNotFound() {
        Long clientId = 1L, carId = 2L;

        Client client = Client.builder()
                .id(clientId)
                .interestedCars(new HashSet<>())
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteInterestedCar(carId, clientId));
    }
    @Test
    @SuppressWarnings("unchecked")
    void testFindUserById_WhenCached() throws Exception {
        Long id = 1L;
        GetClientDTO dto = GetClientDTO.builder().id(id).name("Cached").build();

        Field field = ClientServiceImpl.class.getDeclaredField("clientCache");
        field.setAccessible(true);

        MyCache<Long, GetClientDTO> cache = (MyCache<Long, GetClientDTO>) field.get(clientService);
        cache.put(id, dto);

        GetClientDTO result = clientService.findUserById(id);
        assertEquals(dto, result);
    }
    @Test
    void testUpdateUser_ThrowsIfNotFound() {
        Client client = Client.builder().id(42L).name("Ghost").build();
        when(clientRepository.findById(client.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateUser(client));
    }

    @Test
    void testDeleteUser_ThrowsIfNotFound() {
        Long id = 100L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.deleteUser(id));
    }


}


