package com.project.loan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear un nuevo cliente")
@Data
public class CreateClientDTO {
    
    @Schema(description = "Nombre del cliente")
    @NotNull
    private String name;

    @Schema(description = "DNI del cliente (8 dígitos + 1 letra)")
    @NotNull
    @Pattern(regexp = "^[0-9]{8}[A-Z]$")
    private String dni;
    
    @Schema(description = "Email del cliente")
    @Email
    private String email;

    
}