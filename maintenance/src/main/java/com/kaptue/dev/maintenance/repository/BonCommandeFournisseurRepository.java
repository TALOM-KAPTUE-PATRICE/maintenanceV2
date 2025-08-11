package com.kaptue.dev.maintenance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaptue.dev.maintenance.entity.BonCommandeFournisseur;

public interface BonCommandeFournisseurRepository extends JpaRepository<BonCommandeFournisseur, String> {

    /**
     * Trouve le dernier ID de bon de commande fournisseur pour un préfixe donné.
     * @param prefix Le préfixe de l'ID.
     * @return Le BonCommandeFournisseur avec l'ID le plus élevé pour ce préfixe.
     */
    // ▼▼▼ CORRECTION ICI ▼▼▼
    // On remplace 'BonCommande' par 'BonCommandeFournisseur'
    @Query("SELECT bc FROM BonCommandeFournisseur bc WHERE bc.id LIKE :prefix% ORDER BY bc.id DESC LIMIT 1")
    Optional<BonCommandeFournisseur> findTopByIdStartingWithOrderByIdDesc(@Param("prefix") String prefix);

    // ▼▼▼ CORRECTION ICI ÉGALEMENT ▼▼▼
    @Query("SELECT b FROM BonCommandeFournisseur b JOIN FETCH b.fournisseur f JOIN FETCH b.devis d")
    List<BonCommandeFournisseur> findAllWithFournisseurAndDevis();
    
}