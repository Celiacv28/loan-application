package com.project.loan.services;

import com.project.loan.models.Client;
import com.project.loan.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients(String email, String dni) {
        if (email != null) {
            return clientRepository.findByEmail(email).map(List::of).orElse(List.of());
        } else if (dni != null) {
            return clientRepository.findByDni(dni).map(List::of).orElse(List.of());
        } else {
            return clientRepository.findAll();
        }
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client createClient(Client client) {
        validateEmailUnique(client.getEmail());
        validateDniUnique(client.getDni());
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id)
                .map(client -> {
                    if (!client.getEmail().equals(clientDetails.getEmail())) {
                        validateEmailUnique(clientDetails.getEmail());
                    }
                    if (!client.getDni().equals(clientDetails.getDni())) {
                        validateDniUnique(clientDetails.getDni());
                    }
                    client.setName(clientDetails.getName());
                    client.setEmail(clientDetails.getEmail());
                    client.setDni(clientDetails.getDni());
                    return clientRepository.save(client);
                });
    }

    @Override
    public boolean deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateEmailUnique(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }
    }

    private void validateDniUnique(String dni) {
        if (clientRepository.existsByDni(dni)) {
            throw new RuntimeException("Ya existe un cliente con ese DNI");
        }
    }
}