package com.example.flashcards.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO représentant une carte mémoire (flashcard).
 * <p>
 * Cette classe est utilisée pour échanger des données
 * entre les couches de l'application
 * concernant les questions, réponses et catégories
 * associées.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardDTO {

    /**
     * Identifiant unique de la flashcard.
     */
    private Long id;

    /**
     * Question de la flashcard.
     */
    private String question;

    /**
     * Réponse associée à la question.
     */
    private String answer;

    /**
     * Identifiant de la catégorie à laquelle appartient la flashcard.
     */
    private Long categoryId;
}
