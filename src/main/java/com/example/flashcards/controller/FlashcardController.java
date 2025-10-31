package com.example.flashcards.controller;

import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.service.CategoryService;
import com.example.flashcards.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des flashcards.
 */
@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    /**
     * Service gérant la logique métier liée aux flashcards.
     */
    private final FlashcardService flashcardService;

    /**
     * Service gérant la logique métier liée aux catégories.
     */
    private final CategoryService categoryService;

    /**
     * Récupère toutes les flashcards.
     *
     * @return liste de toutes les flashcards
     */
    @GetMapping
    public List<FlashcardDTO> getAll() {
        return flashcardService.getAllFlashcards()
                .stream()
                .map(FlashcardMapper::toDTO)
                .toList();
    }

    /**
     * Récupère une flashcard par son identifiant.
     *
     * @param id identifiant de la flashcard
     * @return la flashcard correspondante
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlashcardDTO> getById(final @PathVariable Long id) {
        return flashcardService.getFlashcardById(id)
                .map(FlashcardMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Recherche des flashcards par question.
     *
     * @param question texte de la question
     * @return liste des flashcards correspondantes
     */
    @GetMapping("/search")
    public ResponseEntity<List<FlashcardDTO>> searchByQuestion(
            @RequestParam("question") final String question) {
        return ResponseEntity.ok(
                flashcardService.searchByQuestion(question)
        );
    }

    /**
     * Crée une nouvelle flashcard.
     *
     * @param dto données de la flashcard
     * @return la flashcard créée
     */
    @PostMapping
    public FlashcardDTO create(final @RequestBody FlashcardDTO dto) {
        Category category = categoryService.getCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Flashcard saved = flashcardService.createFlashcard(
                FlashcardMapper.toEntity(dto, category)
        );
        return FlashcardMapper.toDTO(saved);
    }

    /**
     * Met à jour une flashcard existante.
     *
     * @param id identifiant de la flashcard
     * @param dto nouvelles données de la flashcard
     * @return la flashcard mise à jour
     */
    @PutMapping("/{id}")
    public FlashcardDTO update(
            @PathVariable final Long id, @RequestBody final FlashcardDTO dto) {
        Category category = categoryService.getCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Flashcard updated = flashcardService.updateFlashcard(
                id, FlashcardMapper.toEntity(dto, category)
        );
        return FlashcardMapper.toDTO(updated);
    }

    /**
     * Supprime une flashcard par son identifiant.
     *
     * @param id identifiant de la flashcard
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        flashcardService.deleteFlashcard(id);
    }
}
