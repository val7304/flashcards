package com.example.flashcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une carte mémoire (flashcard).
 * <p>
 * Cette classe est utilisée pour échanger des données entre les couches
 * de l'application concernant les questions, réponses et catégories
 * associées.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// CHECKSTYLE:OFF: AbbreviationAsWordInName
public class FlashcardDto {
  // CHECKSTYLE:ON: AbbreviationAsWordInName

  /** Identifiant unique de la flashcard. */
  private Long id;

  /** Question de la flashcard. */
  private String question;

  /** Réponse associée à la question. */
  private String answer;

  /** Identifiant de la catégorie à laquelle appartient la flashcard. */
  private Long categoryId;
}
