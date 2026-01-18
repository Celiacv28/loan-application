package com.project.loan.controllers;

import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;
import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.services.LoanRequestService;
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
@DisplayName("LoanRequestController Tests")
class LoanRequestControllerTest {

    @Mock
    private LoanRequestService loanRequestService;

    @InjectMocks
    private LoanRequestControllerImpl loanRequestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private LoanRequest testLoanRequest1;
    private LoanRequest testLoanRequest2;
    private CreateLoanRequestDTO createLoanRequestDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanRequestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Juan Pérez");
        testUser.setType(UserType.CLIENT);
        testUser.setDni("12345678A");
        testUser.setEmail("juan@email.com");
        testUser.setCreatedAt(LocalDateTime.now());

        testLoanRequest1 = new LoanRequest();
        testLoanRequest1.setId(1L);
        testLoanRequest1.setUser(testUser);
        testLoanRequest1.setAmount(15000.0);
        testLoanRequest1.setCurrency("EUR");
        testLoanRequest1.setStatus(LoanStatus.PENDING);
        testLoanRequest1.setCreatedAt(LocalDateTime.now());

        testLoanRequest2 = new LoanRequest();
        testLoanRequest2.setId(2L);
        testLoanRequest2.setUser(testUser);
        testLoanRequest2.setAmount(25000.0);
        testLoanRequest2.setCurrency("USD");
        testLoanRequest2.setStatus(LoanStatus.APPROVED);
        testLoanRequest2.setCreatedAt(LocalDateTime.now());

        createLoanRequestDTO = new CreateLoanRequestDTO();
        createLoanRequestDTO.setUserId(1L);
        createLoanRequestDTO.setAmount(10000.0);
        createLoanRequestDTO.setCurrency("EUR");
    }

    @Test
    @DisplayName("GET all loan requests")
    void testGetAllLoanRequests() throws Exception {
        List<LoanRequest> loanRequests = Arrays.asList(testLoanRequest1, testLoanRequest2);
        when(loanRequestService.getAllLoanRequests(null, null, null)).thenReturn(loanRequests);

        mockMvc.perform(get("/api/loan-requests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(15000.0))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amount").value(25000.0))
                .andExpect(jsonPath("$[1].currency").value("USD"))
                .andExpect(jsonPath("$[1].status").value("APPROVED"));

        verify(loanRequestService).getAllLoanRequests(null, null, null);
    }

    @Test
    @DisplayName("GET loan requests with filters")
    void testGetLoanRequestsWithFilters() throws Exception {
        List<LoanRequest> filteredRequests = Arrays.asList(testLoanRequest1);
        when(loanRequestService.getAllLoanRequests(LoanStatus.PENDING, 1L, "EUR")).thenReturn(filteredRequests);

        mockMvc.perform(get("/api/loan-requests")
                .param("status", "PENDING")
                .param("userId", "1")
                .param("currency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].currency").value("EUR"));

        verify(loanRequestService).getAllLoanRequests(LoanStatus.PENDING, 1L, "EUR");
    }

    @Test
    @DisplayName("GET loan request by ID")
    void testGetLoanRequestById() throws Exception {
        when(loanRequestService.getLoanRequestById(1L)).thenReturn(Optional.of(testLoanRequest1));

        mockMvc.perform(get("/api/loan-requests/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(15000.0))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(loanRequestService).getLoanRequestById(1L);
    }

    @Test
    @DisplayName("GET loan request by ID not found")
    void testGetLoanRequestById_NotFound() throws Exception {
        when(loanRequestService.getLoanRequestById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/loan-requests/999"))
                .andExpect(status().isNotFound());

        verify(loanRequestService).getLoanRequestById(999L);
    }

    @Test
    @DisplayName("POST create a new loan request")
    void testCreateLoanRequest_WithValidData_ShouldCreateRequest() throws Exception {
        LoanRequest savedRequest = new LoanRequest();
        savedRequest.setId(3L);
        savedRequest.setUser(testUser);
        savedRequest.setAmount(createLoanRequestDTO.getAmount());
        savedRequest.setCurrency(createLoanRequestDTO.getCurrency());
        savedRequest.setStatus(LoanStatus.PENDING);
        savedRequest.setCreatedAt(LocalDateTime.now());

        when(loanRequestService.createLoanRequest(any(CreateLoanRequestDTO.class))).thenReturn(savedRequest);

        String jsonContent = objectMapper.writeValueAsString(createLoanRequestDTO);

        mockMvc.perform(post("/api/loan-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.amount").value(10000.0))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(loanRequestService).createLoanRequest(any(CreateLoanRequestDTO.class));
    }

    @Test
    @DisplayName("POST create loan request with invalid data")
    void testCreateLoanRequest_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        when(loanRequestService.createLoanRequest(any(CreateLoanRequestDTO.class)))
                .thenThrow(new RuntimeException("Invalid data"));

        String jsonContent = objectMapper.writeValueAsString(createLoanRequestDTO);

        mockMvc.perform(post("/api/loan-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(loanRequestService).createLoanRequest(any(CreateLoanRequestDTO.class));
    }

    @Test
    @DisplayName("PUT update loan request status")
    void testUpdateLoanRequestStatus() throws Exception {
        LoanRequest updatedRequest = new LoanRequest();
        updatedRequest.setId(1L);
        updatedRequest.setUser(testUser);
        updatedRequest.setAmount(15000.0);
        updatedRequest.setCurrency("EUR");
        updatedRequest.setStatus(LoanStatus.APPROVED);
        updatedRequest.setCreatedAt(LocalDateTime.now());

        when(loanRequestService.updateLoanRequestStatus(eq(1L), eq(LoanStatus.APPROVED)))
                .thenReturn(Optional.of(updatedRequest));

        String jsonContent = objectMapper.writeValueAsString(LoanStatus.APPROVED);

        mockMvc.perform(put("/api/loan-requests/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(loanRequestService).updateLoanRequestStatus(eq(1L), eq(LoanStatus.APPROVED));
    }

    @Test
    @DisplayName("PUT update loan request status not found")
    void testUpdateLoanRequestStatus_NotFound() throws Exception {
        when(loanRequestService.updateLoanRequestStatus(eq(999L), eq(LoanStatus.APPROVED)))
                .thenReturn(Optional.empty());

        String jsonContent = objectMapper.writeValueAsString(LoanStatus.APPROVED);

        mockMvc.perform(put("/api/loan-requests/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isNotFound());

        verify(loanRequestService).updateLoanRequestStatus(eq(999L), eq(LoanStatus.APPROVED));
    }

    @Test
    @DisplayName("PUT update loan request status with exception")
    void testUpdateLoanRequestStatus_WithException_ShouldReturnBadRequest() throws Exception {
        when(loanRequestService.updateLoanRequestStatus(eq(1L), eq(LoanStatus.APPROVED)))
                .thenThrow(new RuntimeException("Status update failed"));

        String jsonContent = objectMapper.writeValueAsString(LoanStatus.APPROVED);

        mockMvc.perform(put("/api/loan-requests/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(loanRequestService).updateLoanRequestStatus(eq(1L), eq(LoanStatus.APPROVED));
    }

    @Test
    @DisplayName("POST create loan request with invalid currency format")
    void testCreateLoanRequest_WithInvalidCurrency_ShouldReturnBadRequest() throws Exception {
        CreateLoanRequestDTO invalidDTO = new CreateLoanRequestDTO();
        invalidDTO.setUserId(1L);
        invalidDTO.setAmount(10000.0);
        invalidDTO.setCurrency("INVALID"); // Divisa inválida

        String jsonContent = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/loan-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(loanRequestService, never()).createLoanRequest(any(CreateLoanRequestDTO.class));
    }

    @Test
    @DisplayName("POST create loan request with negative amount")
    void testCreateLoanRequest_WithNegativeAmount_ShouldReturnBadRequest() throws Exception {
        CreateLoanRequestDTO invalidDTO = new CreateLoanRequestDTO();
        invalidDTO.setUserId(1L);
        invalidDTO.setAmount(-5000.0); // Cantidad negativa
        invalidDTO.setCurrency("EUR");

        String jsonContent = objectMapper.writeValueAsString(invalidDTO);

        mockMvc.perform(post("/api/loan-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(loanRequestService, never()).createLoanRequest(any(CreateLoanRequestDTO.class));
    }
}