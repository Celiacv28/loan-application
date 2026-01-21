package com.project.loan.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.loan.dto.ChangeLoanStatusDTO;
import com.project.loan.dto.CreateLoanRequestDTO;
import com.project.loan.models.LoanRequest;
import com.project.loan.models.LoanStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/loan-requests")
@Tag(name = "Loan Requests", description = "API para gestión de solicitudes de préstamos")
public interface LoanRequestController {

    @GetMapping
    @Operation(summary = "Obtener todas las solicitudes de préstamo", 
               description = "Devuelve una lista de todas las solicitudes con filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida exitosamente")
    ResponseEntity<List<LoanRequest>> getAllLoanRequests(
            @Parameter(description = "Filtrar por estado de la solicitud")
            @RequestParam(required = false) LoanStatus status,
            @Parameter(description = "Filtrar por ID del cliente solicitante")
            @RequestParam(required = false) Long ClientId,
            @Parameter(description = "Filtrar por divisa")
            @RequestParam(required = false) String currency);

    @GetMapping("/{id}")
    @Operation(summary = "Obtener solicitud por ID", 
               description = "Devuelve una solicitud específica por su ID")
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    ResponseEntity<LoanRequest> getLoanRequestById(
            @Parameter(description = "ID de la solicitud", required = true)
            @PathVariable Long id);

    @PostMapping
    @Operation(summary = "Crear nueva solicitud de préstamo", 
               description = "Crea una nueva solicitud de préstamo en el sistema")
    @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente")
    ResponseEntity<LoanRequest> createLoanRequest(
            @Parameter(description = "Datos de la solicitud a crear", required = true)
            @RequestBody CreateLoanRequestDTO createLoanRequestDTO);

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de solicitud", 
               description = "Actualiza el estado de una solicitud de préstamo")
    @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente")
    ResponseEntity<LoanRequest> updateLoanRequestStatus(
            @Parameter(description = "ID de la solicitud", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la solicitud (ejemplo: {\"status\": \"APPROVED\"})", required = true)
            @RequestBody ChangeLoanStatusDTO changeLoanStatusDTO);
}