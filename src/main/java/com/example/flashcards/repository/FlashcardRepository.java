package com.example.flashcards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flashcards.entity.Flashcard;

/**
 * Repository Spring Data JPA pour l'entité {@link Flashcard}.
 *
 * <p>
 * Cette interface fournit des méthodes pour gérer la persistance des
 * flashcards et effectuer des recherches personnalisées.
 * </p>
 */
@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    /**
     * Recherche les flashcards dont la question contient une chaîne donnée
     * sans tenir compte de la casse.
     *
     * @param question partie de la question à rechercher
     * @return liste des flashcards correspondant au critère
     */
    List<Flashcard> findByQuestionContainingIgnoreCase(String question);
}
