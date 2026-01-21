package com.project.loan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.project.loan.dto.CreateClientDTO;
import com.project.loan.mappers.ClientMapper;
import com.project.loan.models.Client;
import com.project.loan.services.ClientService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ClientControllerImpl implements ClientController {
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private ClientMapper userMapper;

    @Override
    public ResponseEntity<List<Client>> getAllClients(String email, String dni) {
        log.info("[GET] getAllClients called with email={}, dni={}", email, dni);
        List<Client> clients = clientService.getAllClients(email, dni);
        return ResponseEntity.ok(clients);
    }

    @Override
    public ResponseEntity<Client> getClientById(Long id) {
        log.info("[GET] getClientById called with id={}", id);
        return clientService.getClientById(id)
                .map(client -> {
                    log.info("[GET] Client found for id={}", id);
                    return ResponseEntity.ok(client);
                })
                .orElseGet(() -> {
                    log.warn("[GET] Client not found for id={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Override
    public ResponseEntity<Client> createClient(@Valid CreateClientDTO createClientDTO) {
        log.info("[POST] createClient called with DTO: {}", createClientDTO);
        try {
            Client client = userMapper.toEntity(createClientDTO);
            Client savedClient = clientService.createClient(client);
            log.info("[POST] Client created with id={}", savedClient.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (RuntimeException e) {
            log.error("[POST] Error creating Client: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Client> updateClient(Long id, @Valid Client clientDetails) {
        log.info("[PUT] updateClient called with id={}, clientDetails={}", id, clientDetails);
        try {
            return clientService.updateClient(id, clientDetails)
                    .map(client -> {
                        log.info("[PUT] Client updated for id={}", id);
                        return ResponseEntity.ok(client);
                    })
                    .orElseGet(() -> {
                        log.warn("[PUT] Client not found for id={}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (RuntimeException e) {
            log.error("[PUT] Error updating User for id={}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteClient(Long id) {
        log.info("[DELETE] deleteClient called with id={}", id);
        if (clientService.deleteClient(id)) {
            log.info("[DELETE] Client deleted for id={}", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("[DELETE] Client not found for id={}", id);
        return ResponseEntity.notFound().build();
    }
}