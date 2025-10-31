package com.example.flashcards.mapper;

import com.example.flashcards.dto.CategoryDTO;
import com.example.flashcards.entity.Category;

/**
 * Classe utilitaire pour mapper entre {@link Category} et
 * {@link CategoryDTO}.
 * <p>
 * Fournit des méthodes statiques pour convertir les entités
 * vers leurs DTOs et inversement.
 * </p>
 */
public final class CategoryMapper {

    /**
     * Constructeur privé pour empêcher l'instanciation.
     * Nécessaire pour respecter la règle Checkstyle
     * "HideUtilityClassConstructorCheck".
     */
    private CategoryMapper() {
        // Constructeur vide intentionnellement.
    }

    /**
     * Convertit une entité {@link Category} en {@link CategoryDTO}.
     *
     * @param category entité {@link Category} à convertir
     * @return un objet {@link CategoryDTO} correspondant, ou
     * {@code null} si l'entrée est {@code null}
     */
    public static CategoryDTO toDTO(final Category category) {
        if (category == null) {
            return null;
        }

        final CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    /**
     * Convertit un objet {@link CategoryDTO} en entité
     * {@link Category}.
     *
     * @param dto objet {@link CategoryDTO} à convertir
     * @return une entité {@link Category} correspondante,
     * ou {@code null} si l'entrée est {@code null}
     */
    public static Category toEntity(final CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        final Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}
