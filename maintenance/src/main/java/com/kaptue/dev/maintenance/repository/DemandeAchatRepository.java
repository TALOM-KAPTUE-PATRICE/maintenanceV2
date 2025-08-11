package com.kaptue.dev.maintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO;
import com.kaptue.dev.maintenance.entity.DemandeAchat;
import com.kaptue.dev.maintenance.entity.enums.DemandeAchatStatus;

public interface DemandeAchatRepository extends JpaRepository<DemandeAchat, Long> {

    /**
     * KPI 5: Compte le nombre de demandes d'achat pour chaque statut.
     *
     * @return Une liste de DTOs pour le graphique.
     */
    @Query("SELECT new com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO(da.statut, COUNT(da)) "
            + "FROM DemandeAchat da "
            + "GROUP BY da.statut")
    List<ChartDataDTO<DemandeAchatStatus>> countByStatus();
}
