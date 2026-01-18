package com.project.loan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.project.loan.dto.CreateUserDTO;
import com.project.loan.mappers.UserMapper;
import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserControllerImpl implements UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<List<User>> getAllUsers(UserType type, String email, String dni) {
        log.info("[GET] getAllUsers called with type={}, email={}, dni={}", type, email, dni);
        List<User> users = userService.getAllUsers(type, email, dni);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        log.info("[GET] getUserById called with id={}", id);
        return userService.getUserById(id)
                .map(user -> {
                    log.info("[GET] User found for id={}", id);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("[GET] User not found for id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Override
    public ResponseEntity<User> createUser(@Valid CreateUserDTO createUserDTO) {
        log.info("[POST] createUser called with DTO: {}", createUserDTO);
        try {
            User user = userMapper.toEntity(createUserDTO);
            User savedUser = userService.createUser(user);
            log.info("[POST] User created with id={}", savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException e) {
            log.error("[POST] Error creating User: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<User> updateUser(Long id, @Valid User userDetails) {
        log.info("[PUT] updateUser called with id={}, userDetails={}", id, userDetails);
        try {
            return userService.updateUser(id, userDetails)
                    .map(user -> {
                        log.info("[PUT] User updated for id={}", id);
                        return ResponseEntity.ok(user);
                    })
                    .orElseGet(() -> {
                        log.warn("[PUT] User not found for id={}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (RuntimeException e) {
            log.error("[PUT] Error updating User for id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        log.info("[DELETE] deleteUser called with id={}", id);
        if (userService.deleteUser(id)) {
            log.info("[DELETE] User deleted for id={}", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("[DELETE] User not found for id={}", id);
        return ResponseEntity.notFound().build();
    }
}