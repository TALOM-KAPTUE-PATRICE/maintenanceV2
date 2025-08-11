package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.BonCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BonCommandeClientRepository extends JpaRepository<BonCommandeClient, Long> {
    // On peut ajouter des requêtes spécifiques plus tard si besoin
}