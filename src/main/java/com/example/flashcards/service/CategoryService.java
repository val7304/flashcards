package com.example.flashcards.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.mapper.CategoryMapper;
import com.example.flashcards.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service gérant la logique métier liée aux catégories de flashcards.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    /**
     * Repository permettant d'accéder aux données des catégories.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Récupère toutes les catégories disponibles.
     *
     * @return liste de toutes les catégories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Recherche les catégories dont le nom contient une chaîne donnée
     * (sans sensibilité à la casse).
     *
     * @param name partie du nom à rechercher
     * @return liste de {@link CategoryDto} correspondant à la recherche
     */
    public List<CategoryDto> searchByName(final String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    /**
     * Récupère une catégorie par son identifiant.
     *
     * @param id identifiant de la catégorie
     * @return un {@link Optional} contenant la catégorie si elle existe
     */
    public Optional<Category> getCategoryById(final Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Crée une nouvelle catégorie.
     *
     * @param category catégorie à créer
     * @return la catégorie enregistrée
     */
    public Category createCategory(final Category category) {
        return categoryRepository.save(category);
    }

    /**
     * Met à jour une catégorie existante.
     *
     * @param id       identifiant de la catégorie à mettre à jour
     * @param category nouvelles informations de la catégorie
     * @return la catégorie mise à jour
     * @throws RuntimeException si la catégorie n'existe pas
     */
    public Category updateCategory(final Long id, final Category category) {
        return categoryRepository.findById(id)
                .map(c -> {
                    c.setName(category.getName());
                    return categoryRepository.save(c);
                })
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    /**
     * Supprime une catégorie selon son identifiant.
     *
     * @param id identifiant de la catégorie à supprimer
     */
    public void deleteCategory(final Long id) {
        categoryRepository.deleteById(id);
    }
}
