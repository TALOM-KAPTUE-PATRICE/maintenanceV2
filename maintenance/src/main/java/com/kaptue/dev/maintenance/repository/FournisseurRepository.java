package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    /**
     * Vérifie si un fournisseur avec l'email donné existe déjà.
     * @param email L'email à vérifier.
     * @return true si l'email existe, false sinon.
     */
    boolean existsByEmailFourniss(String email);

    /**
     * Trouve un fournisseur par son email.
     * @param email L'email du fournisseur à trouver.
     * @return un Optional contenant le fournisseur s'il est trouvé.
     */
    Optional<Fournisseur> findByEmailFourniss(String email);
}