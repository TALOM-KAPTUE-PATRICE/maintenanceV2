package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, String> {

    @Query("SELECT f FROM Facture f WHERE f.id LIKE :prefix% ORDER BY f.id DESC LIMIT 1")
    Optional<Facture> findTopByIdStartingWithOrderByIdDesc(@Param("prefix") String prefix);
}