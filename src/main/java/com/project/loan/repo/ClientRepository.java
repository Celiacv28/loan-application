package com.project.loan.repo;

import com.project.loan.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
    
    Optional<Client> findByDni(String dni);
    
    boolean existsByEmail(String email);
    
    boolean existsByDni(String dni);
}