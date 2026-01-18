package com.project.loan.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column
    private String name;

    @Column(nullable = false)
    @NotNull
    private UserType type;

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
