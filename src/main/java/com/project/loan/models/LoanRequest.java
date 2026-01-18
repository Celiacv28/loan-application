package com.project.loan.models;

import java.time.LocalDateTime;


import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Data
@Entity
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column
    @Positive
    private Double amount;

    @Column
    private String currency;

    @Column
    @NotNull
    private LoanStatus status;

    @Column
    private LocalDateTime createdAt;

}
