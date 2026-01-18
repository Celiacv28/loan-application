package com.project.loan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "DTO para crear una nueva solicitud de préstamo")
public class CreateLoanRequestDTO {
    
    @Schema(description = "ID del usuario que solicita el préstamo")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
    
    @Schema(description = "Importe del préstamo solicitado")
    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser mayor que cero")
    private Double amount;
    
    @Schema(description = "Divisa del préstamo (EUR, USD, GBP)")
    @NotNull(message = "La divisa es obligatoria")
    @Pattern(regexp = "^(EUR|USD|GBP)$", message = "Divisa debe ser EUR, USD o GBP")
    private String currency;
}