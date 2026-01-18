package com.project.loan.controllers;

import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.dto.CreateUserDTO;
import com.project.loan.services.UserService;
import com.project.loan.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<List<User>> getAllUsers(UserType type, String email, String dni) {
        List<User> users = userService.getAllUsers(type, email, dni);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<User> createUser(@Valid CreateUserDTO createUserDTO) {
        try {
            User user = userMapper.toEntity(createUserDTO);
            User savedUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<User> updateUser(Long id, @Valid User userDetails) {
        try {
            return userService.updateUser(id, userDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}