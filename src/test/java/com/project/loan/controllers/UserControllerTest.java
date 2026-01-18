package com.project.loan.controllers;

import com.project.loan.dto.CreateUserDTO;
import com.project.loan.mappers.UserMapper;
import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.services.UserService;
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
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserControllerImpl userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User testUser1;
    private User testUser2;
    private CreateUserDTO createUserDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setName("Juan Pérez");
        testUser1.setType(UserType.CLIENT);
        testUser1.setDni("12345678A");
        testUser1.setEmail("juan@email.com");
        testUser1.setCreatedAt(LocalDateTime.now());

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setName("María García");
        testUser2.setType(UserType.MANAGER);
        testUser2.setDni("87654321B");
        testUser2.setEmail("maria@email.com");
        testUser2.setCreatedAt(LocalDateTime.now());

        createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Pedro López");
        createUserDTO.setType(UserType.CLIENT) ;
        createUserDTO.setDni("11111111C");
        createUserDTO.setEmail("pedro@email.com");
    }

    @Test
    @DisplayName("GET all Users records")
    void testGetAll() throws Exception {
        List<User> users = Arrays.asList(testUser1, testUser2);
        when(userService.getAllUsers(null, null, null)).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].type").value("CLIENT"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("María García"))
                .andExpect(jsonPath("$[1].type").value("MANAGER"));

        verify(userService).getAllUsers(null, null, null);
    }

    @Test
    @DisplayName("GET users with filters")
    void testGetUsersFilter() throws Exception {
        List<User> filteredUsers = Arrays.asList(testUser1);
        when(userService.getAllUsers(null, "juan@email.com", "12345678A")).thenReturn(filteredUsers);

        mockMvc.perform(get("/api/users")
                .param("email", "juan@email.com")
                .param("dni", "12345678A"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("juan@email.com"))
                .andExpect(jsonPath("$[0].dni").value("12345678A"));

        verify(userService).getAllUsers(null, "juan@email.com", "12345678A");
    }

    @Test
    @DisplayName("GET users by ID")
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser1));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@email.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("GET users by ID not found")
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(999L);
    }

    @Test
    @DisplayName("POST create a new user")
    void createUser_WithValidData_ShouldCreateUser() throws Exception {
        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setName(createUserDTO.getName());
        savedUser.setType(createUserDTO.getType());
        savedUser.setDni(createUserDTO.getDni());
        savedUser.setEmail(createUserDTO.getEmail());
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(savedUser);
        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        String jsonContent = objectMapper.writeValueAsString(createUserDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Pedro López"))
                .andExpect(jsonPath("$.type").value("CLIENT"))
                .andExpect(jsonPath("$.dni").value("11111111C"));

        verify(userMapper).toEntity(any(CreateUserDTO.class));
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("POST create user with conflict")
    void testCreateUser_WithConflict() throws Exception {
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(testUser1);
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Usuario ya existe"));

        String jsonContent = objectMapper.writeValueAsString(createUserDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isConflict());

        verify(userMapper).toEntity(any(CreateUserDTO.class));
        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("PUT update existing user")
    void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Juan Pérez Actualizado");
        updatedUser.setType(UserType.MANAGER);
        updatedUser.setDni("12345678A");
        updatedUser.setEmail("juan.updated@email.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        String jsonContent = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.type").value("MANAGER"))
                .andExpect(jsonPath("$.email").value("juan.updated@email.com"));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("PUT update user not found")
    void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(999L), any(User.class))).thenReturn(Optional.empty());

        String jsonContent = objectMapper.writeValueAsString(testUser1);

        mockMvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(999L), any(User.class));
    }

    @Test
    @DisplayName("DELETE delete existing user")
    void testDeleteUser() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("DELETE delete user not found")
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(999L);
    }

    @Test
    @DisplayName("POST create user with invalid DNI format")
    void testCreateUser_WithInvalidDNI_ShouldReturnBadRequest() throws Exception {
        CreateUserDTO invalidDTO = new CreateUserDTO();
        invalidDTO.setName("Test User");
        invalidDTO.setType(UserType.CLIENT);
        invalidDTO.setDni("12345678"); // DNI sin letra
        invalidDTO.setEmail("test@email.com");

        String jsonContent = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(userMapper, never()).toEntity(any(CreateUserDTO.class));
        verify(userService, never()).createUser(any(User.class));
    }
  
}