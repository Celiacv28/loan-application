package com.project.loan.controllers;

import com.project.loan.dto.CreateClientDTO;
import com.project.loan.mappers.ClientMapper;
import com.project.loan.models.Client;
import com.project.loan.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientController Tests")
class ClientControllerTest {

    @Mock
    private ClientService clientService;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientControllerImpl clientController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Client testClient1;
    private Client testClient2;
    private CreateClientDTO createClientDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testClient1 = new Client();
        testClient1.setId(1L);
        testClient1.setName("Juan Pérez");
        testClient1.setDni("12345678A");
        testClient1.setEmail("juan@email.com");
        testClient1.setCreatedAt(LocalDateTime.now());

        testClient2 = new Client();
        testClient2.setId(2L);
        testClient2.setName("María García");
        testClient2.setDni("87654321B");
        testClient2.setEmail("maria@email.com");
        testClient2.setCreatedAt(LocalDateTime.now());

        createClientDTO = new CreateClientDTO();
        createClientDTO.setName("Pedro López");
        createClientDTO.setDni("11111111C");
        createClientDTO.setEmail("pedro@email.com");
    }

    @Test
    @DisplayName("GET all Users records")
    void testGetAll() throws Exception {
        List<Client> users = Arrays.asList(testClient1, testClient2);
        when(clientService.getAllClients(null, null)).thenReturn(users);

        mockMvc.perform(get("/api/client"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("María García"));

        verify(clientService).getAllClients( null, null);
    }

    @Test
    @DisplayName("GET users with filters")
    void testGetClientsFilter() throws Exception {
        List<Client> filteredClients = Arrays.asList(testClient1);
        when(clientService.getAllClients("juan@email.com", "12345678A")).thenReturn(filteredClients);

        mockMvc.perform(get("/api/client")
                .param("email", "juan@email.com")
                .param("dni", "12345678A"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("juan@email.com"))
                .andExpect(jsonPath("$[0].dni").value("12345678A"));

        verify(clientService).getAllClients("juan@email.com", "12345678A");
    }

    @Test
    @DisplayName("GET clients by ID")
    void testGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(testClient1));

        mockMvc.perform(get("/api/client/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@email.com"));

        verify(clientService).getClientById(1L);
    }

    @Test
    @DisplayName("GET clients by ID not found")
    void testGetClientById_NotFound() throws Exception {
        when(clientService.getClientById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/client/999"))
                .andExpect(status().isNotFound());

        verify(clientService).getClientById(999L);
    }

    @Test
    @DisplayName("POST create a new client")
    void createClient_WithValidData_ShouldCreateClient() throws Exception {
        Client savedClient = new Client();
        savedClient.setId(3L);
        savedClient.setName(createClientDTO.getName());
        savedClient.setDni(createClientDTO.getDni());
        savedClient.setEmail(createClientDTO.getEmail());
        savedClient.setCreatedAt(LocalDateTime.now());

        when(clientMapper.toEntity(any(CreateClientDTO.class))).thenReturn(savedClient);
        when(clientService.createClient(any(Client.class))).thenReturn(savedClient);

        String jsonContent = objectMapper.writeValueAsString(createClientDTO);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Pedro López"))
                .andExpect(jsonPath("$.dni").value("11111111C"));

        verify(clientMapper).toEntity(any(CreateClientDTO.class));
        verify(clientService).createClient(any(Client.class));
    }

    @Test
    @DisplayName("POST create client with conflict")
    void testCreateClient_WithConflict() throws Exception {
        when(clientMapper.toEntity(any(CreateClientDTO.class))).thenReturn(testClient1);
        when(clientService.createClient(any(Client.class))).thenThrow(new RuntimeException("Cliente ya existe"));

        String jsonContent = objectMapper.writeValueAsString(createClientDTO);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isConflict());

        verify(clientMapper).toEntity(any(CreateClientDTO.class));
        verify(clientService).createClient(any(Client.class));
    }

    @Test
    @DisplayName("PUT update existing client")
    void testUpdateClient() throws Exception {
        Client updatedClient = new Client();
        updatedClient.setId(1L);
        updatedClient.setName("Juan Pérez Actualizado");
        updatedClient.setDni("12345678A");
        updatedClient.setEmail("juan.updated@email.com");

        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(Optional.of(updatedClient));
        String jsonContent = objectMapper.writeValueAsString(updatedClient);

        mockMvc.perform(put("/api/client/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.email").value("juan.updated@email.com"));

        verify(clientService).updateClient(eq(1L), any(Client.class));
    }

    @Test
    @DisplayName("PUT update client not found")
    void testUpdateClient_NotFound() throws Exception {
        when(clientService.updateClient(eq(999L), any(Client.class))).thenReturn(Optional.empty());
        String jsonContent = objectMapper.writeValueAsString(testClient1);

        mockMvc.perform(put("/api/client/999")
                .contentType(MediaType.APPLICATION_JSON)
                
                .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(clientService).updateClient(eq(999L), any(Client.class));
    }

    @Test
    @DisplayName("DELETE delete existing client")
    void testDeleteClient() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/client/1"))
                .andExpect(status().isNoContent());

        verify(clientService).deleteClient(1L);
    }

    @Test
    @DisplayName("DELETE delete client not found")
    void testDeleteClient_NotFound() throws Exception {
        when(clientService.deleteClient(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/client/999"))
                .andExpect(status().isNotFound());

        verify(clientService).deleteClient(999L);
    }

    @Test
    @DisplayName("POST create client with invalid DNI format")
    void testCreateClient_WithInvalidDNI_ShouldReturnBadRequest() throws Exception {
        CreateClientDTO invalidDTO = new CreateClientDTO();
        invalidDTO.setName("Test Client");
        invalidDTO.setDni("12345678"); // DNI sin letra
        invalidDTO.setEmail("test@email.com");

        String jsonContent = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(clientMapper, never()).toEntity(any(CreateClientDTO.class));
        verify(clientService, never()).createClient(any(Client.class));
    }
  
}