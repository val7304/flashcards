package com.example.flashcards.controller;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.mapper.CategoryMapper;
import com.example.flashcards.service.CategoryService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Contrôleur REST pour la gestion des catégories de flashcards. */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  /** Service gérant la logique métier liée aux catégories. */
  private final CategoryService categoryService;

  /**
   * Constructeur avec injection du service.
   *
   * @param categoryService service de gestion des catégories
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Spring injects immutable service beans safely")
  @Autowired
  public CategoryController(final CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Récupère toutes les catégories.
   *
   * @return liste des catégories
   */
  @GetMapping
  public List<CategoryDto> getAll() {
    return categoryService.getAllCategories().stream().map(CategoryMapper::toDto).toList();
  }

  /**
   * Récupère une catégorie par son identifiant.
   *
   * @param id identifiant de la catégorie
   * @return la catégorie correspondante
   */
  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getById(@PathVariable final Long id) {
    return categoryService
        .getCategoryById(id)
        .map(CategoryMapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Recherche une catégorie par son nom.
   *
   * @param name nom de la catégorie
   * @return liste des catégories correspondantes
   */
  @GetMapping("/search")
  public ResponseEntity<List<CategoryDto>> searchByName(@RequestParam("name") final String name) {
    return ResponseEntity.ok(categoryService.searchByName(name));
  }

  /**
   * Crée une catégorie.
   *
   * @param dto nom de la catégorie
   * @return catégorie créée
   */
  @PostMapping
  public CategoryDto create(@RequestBody final CategoryDto dto) {
    Category saved = categoryService.createCategory(CategoryMapper.toEntity(dto));
    return CategoryMapper.toDto(saved);
  }

  /**
   * Met à jour une catégorie.
   *
   * @param id identifiant de la catégorie
   * @param dto données mises à jour
   * @return catégorie mise à jour
   */
  @PutMapping("/{id}")
  public CategoryDto update(@PathVariable final Long id, @RequestBody final CategoryDto dto) {
    Category updated = categoryService.updateCategory(id, CategoryMapper.toEntity(dto));
    return CategoryMapper.toDto(updated);
  }

  /**
   * Supprime une catégorie par son identifiant.
   *
   * @param id identifiant de la catégorie
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    categoryService.deleteCategory(id);
  }
}
