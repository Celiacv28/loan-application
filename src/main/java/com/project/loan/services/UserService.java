package com.project.loan.services;

import com.project.loan.models.User;
import com.project.loan.models.UserType;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> getAllUsers(UserType type, String email, String dni);
    
    Optional<User> getUserById(Long id);
    
    User createUser(User user);
    
    Optional<User> updateUser(Long id, User userDetails);
    
    boolean deleteUser(Long id);
}