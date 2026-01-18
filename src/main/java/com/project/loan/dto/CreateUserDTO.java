package com.project.loan.dto;

import com.project.loan.models.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear un nuevo usuario")
@Data
public class CreateUserDTO {
    
    @Schema(description = "Nombre del usuario")
    @NotNull
    private String name;
    
    @Schema(description = "Tipo de usuario: 'client' o 'manager'")
    @NotNull
    private UserType type;
    
    @Schema(description = "DNI del usuario (8 dígitos + 1 letra)")
    @NotNull
    @Pattern(regexp = "^[0-9]{8}[A-Z]$")
    private String dni;
    
    @Schema(description = "Email del usuario")
    @Email
    private String email;

    
}