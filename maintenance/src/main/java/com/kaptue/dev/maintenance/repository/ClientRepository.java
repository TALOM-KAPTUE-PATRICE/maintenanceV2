package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Vérifie si un client avec l'email donné existe déjà.
     * @param email L'email à vérifier.
     * @return true si l'email existe, false sinon.
     */
    boolean existsByEmail(String email);

    /**
     * Trouve un client par son email.
     * @param email L'email du client à trouver.
     * @return un Optional contenant le client s'il est trouvé.
     */
    Optional<Client> findByEmail(String email);
}