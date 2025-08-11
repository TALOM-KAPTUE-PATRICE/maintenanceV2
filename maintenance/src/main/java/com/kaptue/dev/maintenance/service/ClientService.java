package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.request.CreateClientRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateClientRequest;
import com.kaptue.dev.maintenance.controller.response.ClientResponseDTO;
import com.kaptue.dev.maintenance.entity.Client;
import com.kaptue.dev.maintenance.exception.DuplicateResourceException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.ClientRepository;
import java.util.stream.Collectors;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public ClientResponseDTO createClient(CreateClientRequest request) {
        // Valider que l'email n'est pas déjà utilisé
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un client avec l'email '" + request.getEmail() + "' existe déjà.");
        }

        Client client = new Client();
        client.setNom(request.getNom());
        client.setEmail(request.getEmail());
        client.setNumero(request.getNumero());

        Client savedClient = clientRepository.save(client);
        logger.info("Nouveau client créé avec l'ID: {}", savedClient.getId());

        return ClientResponseDTO.fromEntity(savedClient);
    }

    @Transactional
    public ClientResponseDTO updateClient(Long id, UpdateClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));

        // Vérifier si le nouvel email est utilisé par un AUTRE client
        clientRepository.findByEmail(request.getEmail()).ifPresent(existingClient -> {
            if (!existingClient.getId().equals(id)) {
                throw new DuplicateResourceException("L'email '" + request.getEmail() + "' est déjà utilisé par un autre client.");
            }
        });

        client.setNom(request.getNom());
        client.setEmail(request.getEmail());
        client.setNumero(request.getNumero());

        Client updatedClient = clientRepository.save(client);
        logger.info("Client ID: {} mis à jour.", updatedClient.getId());

        return ClientResponseDTO.fromEntity(updatedClient);
    }

        /**
     * NOUVELLE MÉTHODE : Retourne la liste complète de tous les clients.
     * Idéal pour les listes déroulantes où la pagination n'est pas nécessaire.
     * @return Une liste de ClientResponseDTO.
     */
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> findAllPaginated(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(ClientResponseDTO::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        return ClientResponseDTO.fromEntity(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
        }
        // Ajouter ici une logique pour vérifier si le client a des tickets/factures avant de supprimer ?
        // Par exemple : if (ticketRepository.existsByClientId(id)) { throw new BadRequestException(...) }
        clientRepository.deleteById(id);
        logger.info("Client ID: {} supprimé.", id);
    }
}