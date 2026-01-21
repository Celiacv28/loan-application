package com.project.loan.services;

import com.project.loan.models.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    
    List<Client> getAllClients(String email, String dni);
    
    Optional<Client> getClientById(Long id);
    
    Client createClient(Client client);
    
    Optional<Client> updateClient(Long id, Client clientDetails);
    
    boolean deleteClient(Long id);
}