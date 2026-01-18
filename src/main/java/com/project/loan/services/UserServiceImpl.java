package com.project.loan.services;

import com.project.loan.models.User;
import com.project.loan.models.UserType;
import com.project.loan.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers(UserType type, String email, String dni) {
        if (type != null) {
            return userRepository.findByType(type);
        } else if (email != null) {
            return userRepository.findByEmail(email).map(List::of).orElse(List.of());
        } else if (dni != null) {
            return userRepository.findByDni(dni).map(List::of).orElse(List.of());
        } else {
            return userRepository.findAll();
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        validateEmailUnique(user.getEmail());
        validateDniUnique(user.getDni());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getEmail().equals(userDetails.getEmail())) {
                        validateEmailUnique(userDetails.getEmail());
                    }
                    if (!user.getDni().equals(userDetails.getDni())) {
                        validateDniUnique(userDetails.getDni());
                    }
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setDni(userDetails.getDni());
                    user.setType(userDetails.getType());
                    return userRepository.save(user);
                });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
    }

    private void validateDniUnique(String dni) {
        if (userRepository.existsByDni(dni)) {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        }
    }
}