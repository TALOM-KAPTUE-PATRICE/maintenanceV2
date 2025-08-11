package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime; // Changer LocalDate en LocalDateTime
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // La colonne dateCreation dans Ticket est LocalDateTime
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.dateCreation BETWEEN :startDate AND :endDate")
    Long countByDateCreationBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Récupère tous les tickets en joignant les informations du client pour
     * éviter les requêtes N+1.
     *
     * @return Une liste de tickets avec leurs clients pré-chargés.
     */
    @Query("SELECT t FROM Ticket t JOIN FETCH t.client")
    List<Ticket> findAllWithClient();

}
