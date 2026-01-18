package com.project.loan.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.dto.CreateUserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RequestMapping("/api/users")
@Tag(name = "Users", description = "API para gestión de usuarios")
public interface UserController {

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", 
               description = "Devuelve una lista de todos los usuarios con filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    ResponseEntity<List<User>> getAllUsers(
            @Parameter(description = "Filtrar por tipo de usuario (client/manager)")
            @RequestParam(required = false) UserType type,
            @Parameter(description = "Filtrar por email del usuario")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filtrar por DNI del usuario")
            @RequestParam(required = false) String dni);

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", 
               description = "Devuelve un usuario específico por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    ResponseEntity<User> getUserById(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id);

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", 
               description = "Crea un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    ResponseEntity<User> createUser(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody CreateUserDTO createUserDTO);

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", 
               description = "Actualiza un usuario existente por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    ResponseEntity<User> updateUser(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @RequestBody User user);

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", 
               description = "Elimina un usuario por su ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable Long id);
}