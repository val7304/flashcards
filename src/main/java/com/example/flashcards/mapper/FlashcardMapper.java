package com.example.flashcards.mapper;

import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.entity.Category;

/**
 * Classe utilitaire pour mapper entre {@link Flashcard} et
 * {@link FlashcardDTO}.
 * <p>
 * Fournit des méthodes statiques pour convertir les entités
 * vers leurs DTOs et inversement.
 * </p>
 */
public final class FlashcardMapper {

    /**
     * Constructeur privé pour empêcher l'instanciation.
     * Nécessaire pour respecter la règle Checkstyle
     * "HideUtilityClassConstructorCheck".
     */
    private FlashcardMapper() {
        // Constructeur vide intentionnellement.
    }

    /**
     * Convertit une entité {@link Flashcard}
     * en {@link FlashcardDTO}.
     *
     * @param flashcard entité {@link Flashcard} à convertir
     * @return un objet {@link FlashcardDTO} correspondant,
     * ou {@code null} si l'entrée est {@code null}
     */
    public static FlashcardDTO toDTO(final Flashcard flashcard) {
        if (flashcard == null) {
            return null;
        }

        final FlashcardDTO dto = new FlashcardDTO();
        dto.setId(flashcard.getId());
        dto.setQuestion(flashcard.getQuestion());
        dto.setAnswer(flashcard.getAnswer());
        dto.setCategoryId(
            flashcard.getCategory() != null
            ? flashcard.getCategory().getId() : null
        );
        return dto;
    }
    /**
     * Convertit un {@link FlashcardDTO} en entité
     * {@link Flashcard}.
     *
     * @param dto       objet {@link FlashcardDTO} à convertir
     * @param category  catégorie associée à la flashcard
     * @return une entité {@link Flashcard} correspondante,
     * ou {@code null} si {@code dto} est {@code null}
     */
    public static Flashcard toEntity(final FlashcardDTO dto,
    final Category category) {
        if (dto == null) {
            return null;
        }

        final Flashcard flashcard = new Flashcard();
        flashcard.setId(dto.getId());
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setAnswer(dto.getAnswer());
        flashcard.setCategory(category);
        return flashcard;
    }
}
