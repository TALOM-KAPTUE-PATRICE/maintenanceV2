package com.kaptue.dev.maintenance.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;

import java.util.List;
import java.util.Optional;

public interface DevisRepository extends JpaRepository<Devis , String> {

       // Pour la génération d'ID
    @Query("SELECT d FROM Devis d WHERE d.id LIKE CONCAT('D-', :year, '-', :month, '-%')")
    List<Devis> findByIdStartingWith(@Param("year") String year, @Param("month") String month);

        /**
     * KPI 2: Compte les devis pour un statut donné et les regroupe par nom de client.
     * Le constructeur de ChartDataDTO est appelé directement dans la requête JPQL.
     * @param status Le statut des devis à compter (ex: SOUMIS).
     * @return Une liste de DTOs pour le graphique.
     */
    @Query("SELECT new com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO(d.client.nom, COUNT(d)) " +
           "FROM Devis d " +
           "WHERE d.statut = :status AND d.client IS NOT NULL " +
           "GROUP BY d.client.nom")
    List<ChartDataDTO<String>> countByStatusAndGroupByClient(@Param("status") DevisStatus status);

    /**
     * KPI 3: Compte le nombre de devis pour chaque statut.
     * @return Une liste de DTOs pour le graphique.
     */
    @Query("SELECT new com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO(d.statut, COUNT(d)) " +
           "FROM Devis d " +
           "GROUP BY d.statut")
    List<ChartDataDTO<DevisStatus>> countByStatus();
    
    /**
     * KPI 4: Compte le nombre de devis qui ont un bon de commande client associé.
     * Note: nécessite que vous ayez créé la relation OneToOne vers BonCommandeClient dans l'entité Devis.
     * @return Le nombre de devis avec un PO client.
     */
    @Query("SELECT COUNT(d) FROM Devis d WHERE d.bonCommandeClient IS NOT NULL")
    long countByBonCommandeClientIsNotNull();

    /**
     * KPI 4: Compte le nombre de devis ayant un statut spécifique mais PAS de bon de commande client.
     * @param status Le statut à vérifier (ex: VALIDER).
     * @return Le nombre de devis correspondants.
     */
    @Query("SELECT COUNT(d) FROM Devis d WHERE d.statut = :status AND d.bonCommandeClient IS NULL")
    long countByStatusAndBonCommandeClientIsNull(@Param("status") DevisStatus status);

        /**
     * Trouve le dernier devis pour un préfixe donné (ex: "D-2024-07-").
     */
    Optional<Devis> findTopByIdStartingWithOrderByIdDesc(String prefix);

}
