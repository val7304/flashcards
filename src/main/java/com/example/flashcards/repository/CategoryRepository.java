package com.example.flashcards.repository;

import com.example.flashcards.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA pour l'entité {@link Category}.
 *
 * <p>Cette interface permet d'effectuer des opérations CRUD
 * et des requêtes personnalisées sur les catégories.</p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Recherche les catégories dont le nom contient une chaîne donnée,
   * sans tenir compte de la casse.
   *
   * @param name partie du nom à rechercher
   * @return liste des catégories correspondant au critère
   */
  List<Category> findByNameContainingIgnoreCase(String name);
}
