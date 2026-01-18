package com.project.loan.dto;

import com.project.loan.models.LoanStatus;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "DTO para cambiar estado de prestamo")
public class ChangeLoanStatusDTO {

    private LoanStatus status;

}
