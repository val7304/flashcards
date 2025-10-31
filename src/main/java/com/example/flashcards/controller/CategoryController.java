package com.example.flashcards.controller;

import com.example.flashcards.dto.CategoryDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.mapper.CategoryMapper;
import com.example.flashcards.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Contrôleur REST pour la gestion des catégories de flashcards.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public final class CategoryController {
    /**
     * Service gérant la logique métier liée aux catégories.
     */
    private final CategoryService categoryService;

    /**
     * Récupère toutes les catégories.
     *
     * @return liste des catégories
    */
    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories()
                .stream()
                .map(CategoryMapper::toDTO)
                .toList();
                }
    /**
     * Récupère une catégorie par son identifiant.
     *
     * @param id identifiant de la catégorie
     * @return la catégorie correspondante
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable final Long id) {
        return categoryService.getCategoryById(id)
                .map(CategoryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
                }
    /**
     * Recherche une catégorie par son nom.
     *
     * @param name nom de la catégorie
     * @return le nom de service
     */
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchByName(
            @RequestParam("name") final String name) {
        return ResponseEntity.ok(
            categoryService.searchByName(name)
            );
            }
    /**
     * Crée une catégorie par son nom.
     *
     * @param dto nom de la catégorie
     * @return category mapper
     */
    @PostMapping
    public CategoryDTO create(@RequestBody final CategoryDTO dto) {
        Category saved = categoryService.createCategory(
            CategoryMapper.toEntity(dto)
            );
        return CategoryMapper.toDTO(saved);
        }
    /**
     * change mapping.
     *
     * @param id identifiant de la catégorie
     * @param dto de la catégorie
     * @return la catégorie mapper
     */
    @PutMapping("/{id}")
    public CategoryDTO update(
        @PathVariable final Long id, @RequestBody final CategoryDTO dto) {
        Category updated = categoryService.updateCategory(
            id, CategoryMapper.toEntity(dto));
            return CategoryMapper.toDTO(updated);
        }
    /**
     * supprime une catégorie par son identifiant.
     *
     * @param id identifiant de la catégorie
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        categoryService.deleteCategory(id);
        }
}
