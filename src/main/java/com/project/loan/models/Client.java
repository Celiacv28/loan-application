package com.project.loan.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column
    private String name;

    @Column(unique = true)
    @NotNull
    @Pattern(regexp = "^[0-9]{8}[A-Z]$")
    private String dni;

    @Email
    @Column(unique = true)
    private String email;

    @Column
    private LocalDateTime createdAt;


}
