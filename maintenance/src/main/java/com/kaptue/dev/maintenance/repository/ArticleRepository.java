package com.kaptue.dev.maintenance.repository;

import com.kaptue.dev.maintenance.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Vérifie si un article avec la même désignation existe déjà (pourrait être utile
     * pour éviter les doublons).
     * @param designation La désignation à vérifier.
     * @return true si un article avec ce nom existe, false sinon.
     */
    boolean existsByDesignation(String designation);
}