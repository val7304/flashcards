package com.example.flashcards.controller;

import com.example.flashcards.dto.FlashcardDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.service.CategoryService;
import com.example.flashcards.service.FlashcardService;
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

/**
 * Contrôleur REST pour la gestion des flashcards.
 */
@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

  /** Service pour la gestion des flashcards. */
  private final FlashcardService flashcardService;

  /** Service pour la gestion des catégories. */
  private final CategoryService categoryService;

  /**
   * Constructeur avec injection de dépendances.
   *
   * @param flashcardService service gérant les flashcards
   * @param categoryService service gérant les catégories
   */
  // CHECKSTYLE:OFF: ParameterAssignment
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "Spring injects immutable service beans safely"
  )
  @Autowired
  public FlashcardController(final FlashcardService flashcardService,
                             final CategoryService categoryService) {
    this.flashcardService = flashcardService;
    this.categoryService = categoryService;
  }
  // CHECKSTYLE:ON: ParameterAssignment

  /**
   * Récupère toutes les flashcards.
   *
   * @return liste des flashcards
   */
  @GetMapping
  public List<FlashcardDto> getAll() {
    return flashcardService.getAllFlashcards()
        .stream()
        .map(FlashcardMapper::toDto)
        .toList();
  }

  /**
   * Récupère une flashcard par son identifiant.
   *
   * @param id identifiant de la flashcard
   * @return la flashcard correspondante
   */
  @GetMapping("/{id}")
  public ResponseEntity<FlashcardDto> getById(@PathVariable final Long id) {
    return flashcardService.getFlashcardById(id)
        .map(FlashcardMapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Recherche des flashcards par leur question.
   *
   * @param question texte de la question à rechercher
   * @return liste des flashcards correspondantes
   */
  @GetMapping("/search")
  public ResponseEntity<List<FlashcardDto>> searchByQuestion(
      @RequestParam("question") final String question) {
    return ResponseEntity.ok(flashcardService.searchByQuestion(question));
  }

  /**
   * Crée une nouvelle flashcard.
   *
   * @param dto données de la flashcard à créer
   * @return flashcard créée
   */
  @PostMapping
  public FlashcardDto create(@RequestBody final FlashcardDto dto) {
    Category category = categoryService.getCategoryById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    Flashcard saved = flashcardService.createFlashcard(
        FlashcardMapper.toEntity(dto, category));
    return FlashcardMapper.toDto(saved);
  }

  /**
   * Met à jour une flashcard existante.
   *
   * @param id identifiant de la flashcard à mettre à jour
   * @param dto nouvelles données de la flashcard
   * @return flashcard mise à jour
   */
  @PutMapping("/{id}")
  public FlashcardDto update(
      @PathVariable final Long id,
      @RequestBody final FlashcardDto dto) {
    Category category = categoryService.getCategoryById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    Flashcard updated = flashcardService.updateFlashcard(
        id, FlashcardMapper.toEntity(dto, category));
    return FlashcardMapper.toDto(updated);
  }

  /**
   * Supprime une flashcard par son identifiant.
   *
   * @param id identifiant de la flashcard à supprimer
   */
  @DeleteMapping("/{id}")
  public void delete(@PathVariable final Long id) {
    flashcardService.deleteFlashcard(id);
  }
}
