package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.request.CreateClientRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateClientRequest;
import com.kaptue.dev.maintenance.controller.response.ClientResponseDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients") // URI standard avec préfixe /api
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    @PreAuthorize("hasAuthority('" + Permission.CLIENT_CREATE + "')")
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody CreateClientRequest request) {
        ClientResponseDTO createdClient = clientService.createClient(request);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.CLIENT_UPDATE + "')")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @Valid @RequestBody UpdateClientRequest request) {
        ClientResponseDTO updatedClient = clientService.updateClient(id, request);
        return ResponseEntity.ok(updatedClient);
    }

    /**
     * Retourne la liste de tous les clients.
     * Pour les listes déroulantes, la pagination est gérée côté client ou pas du tout.
     */
    @GetMapping
    // On assume que tout utilisateur créant un ticket peut voir la liste des clients.
    // Vous pouvez ajouter une permission plus fine comme 'CLIENT_LIST_READ'
    // @PreAuthorize("hasAuthority(...)")
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }
    
    
    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('" + Permission.CLIENT_READ_OWN + "')")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Permission.CLIENT_DELETE + "')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}