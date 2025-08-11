package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Utiliser Optional pour mieux gérer l'absence

    boolean existsByEmail(String email); // Utile pour vérifier l'unicité
}