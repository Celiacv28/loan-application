package com.project.loan.repo;

import com.project.loan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.loan.models.UserType;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByDni(String dni);
    
    List<User> findByType(UserType type);
    
    boolean existsByEmail(String email);
    
    boolean existsByDni(String dni);
}