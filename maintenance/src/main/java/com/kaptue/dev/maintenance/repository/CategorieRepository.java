package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    /**
     * Vérifie si une catégorie avec le même nom existe déjà.
     * @param nomCategorie Le nom à vérifier.
     * @return true si le nom existe, false sinon.
     */
    boolean existsByNomCategorie(String nomCategorie);
}