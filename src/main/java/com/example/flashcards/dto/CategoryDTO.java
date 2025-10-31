package com.example.flashcards.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO représentant une catégorie de flashcards.
 * <p>
 * Cette classe est utilisée pour transférer les
 * données entre la couche de persistance
 * et la couche de présentation, sans exposer directement
 * l'entité {@code Category}.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    /**
     * Identifiant unique de la catégorie.
     */
    private Long id;

    /**
     * Nom de la catégorie.
     */
    private String name;
}
