package com.example.flashcards.mapper;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;

/**
 * Classe utilitaire pour mapper entre {@link Category} et {@link CategoryDto}.
 *
 * <p>
 * Fournit des méthodes statiques pour convertir les entités vers leurs DTOs et inversement.
 * </p>
 */
public final class CategoryMapper {

  /**
   * Constructeur privé pour empêcher l'instanciation. Nécessaire pour respecter la règle Checkstyle
   * "HideUtilityClassConstructorCheck".
   */
  private CategoryMapper() {
    // Constructeur vide intentionnellement.
  }

  /**
   * Convertit une entité {@link Category} en {@link CategoryDto}.
   *
   * @param category entité {@link Category} à convertir
   * @return un objet {@link CategoryDto} correspondant, ou {@code null} si l'entrée est
   *         {@code null}
   */
  public static CategoryDto toDto(final Category category) {
    if (category == null) {
      return null;
    }

    final CategoryDto dto = new CategoryDto();
    dto.setId(category.getId());
    dto.setName(category.getName());
    return dto;
  }

  /**
   * Convertit un objet {@link CategoryDto} en entité {@link Category}.
   *
   * @param dto objet {@link CategoryDto} à convertir
   * @return une entité {@link Category} correspondante, ou {@code null} si l'entrée est
   *         {@code null}
   */
  public static Category toEntity(final CategoryDto dto) {
    if (dto == null) {
      return null;
    }

    final Category category = new Category();
    category.setId(dto.getId());
    category.setName(dto.getName());
    return category;
  }
}
