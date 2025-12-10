package com.example.flashcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant une catégorie de flashcards.
 *
 * <p>Cette classe est utilisée pour transférer les données entre la couche de persistance et la
 * couche de présentation, sans exposer directement l'entité {@code Category}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// CHECKSTYLE:OFF: AbbreviationAsWordInName
public class CategoryDto {
  // CHECKSTYLE:ON: AbbreviationAsWordInName

  /** Identifiant unique de la catégorie. */
  private Long id;

  /** Nom de la catégorie. */
  private String name;
}
