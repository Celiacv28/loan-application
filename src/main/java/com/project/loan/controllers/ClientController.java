package com.project.loan.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.loan.models.Client;
import com.project.loan.dto.CreateClientDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RequestMapping("/api/client")
@Tag(name = "Client", description = "API para gestión de usuarios")
public interface ClientController {

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", 
               description = "Devuelve una lista de todos los usuarios con filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    ResponseEntity<List<Client>> getAllClients(
            @Parameter(description = "Filtrar por email del cliente")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filtrar por DNI del cliente")
            @RequestParam(required = false) String dni);

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", 
               description = "Devuelve un cliente específico por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    ResponseEntity<Client> getClientById(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Long id);

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", 
               description = "Crea un nuevo cliente en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    ResponseEntity<Client> createClient(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @RequestBody CreateClientDTO createClientDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", 
               description = "Actualiza un cliente existente por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    ResponseEntity<Client> updateClient(
            @Parameter(description = "ID del cliente a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del cliente", required = true)
            @RequestBody Client client);

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", 
               description = "Elimina un cliente por su ID")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente")
    ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID del cliente a eliminar", required = true)
            @PathVariable Long id);
}