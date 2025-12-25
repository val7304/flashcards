package com.example.flashcards.service;

import com.example.flashcards.dto.FlashcardDto;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.repository.FlashcardRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service gérant la logique métier liée aux flashcards. */
@Service
@RequiredArgsConstructor
public class FlashcardService {

  /** Repository permettant d'accéder aux données des flashcards. */
  private final FlashcardRepository flashcardRepository;

  /**
   * Récupère toutes les flashcards disponibles.
   *
   * @return liste de toutes les flashcards
   */
  public List<Flashcard> getAllFlashcards() {
    return flashcardRepository.findAllWithCategory();
  }

  /**
   * Récupère une flashcard selon son identifiant.
   *
   * @param id identifiant de la flashcard
   * @return un {@link Optional} contenant la flashcard si elle existe
   */
  public Optional<Flashcard> getFlashcardById(final Long id) {
    return flashcardRepository.findById(id);
  }

  /**
   * Recherche les flashcards dont la question contient une chaîne donnée (insensible à la casse).
   *
   * @param question partie de la question à rechercher
   * @return liste de {@link FlashcardDto} correspondant au critère
   */
  public List<FlashcardDto> searchByQuestion(final String question) {
    return flashcardRepository.findByQuestionContainingIgnoreCase(question).stream()
        .map(FlashcardMapper::toDto)
        .toList();
  }

  /**
   * Crée une nouvelle flashcard.
   *
   * @param flashcard flashcard à enregistrer
   * @return la flashcard créée
   */
  public Flashcard createFlashcard(final Flashcard flashcard) {
    return flashcardRepository.save(flashcard);
  }

  /**
   * Met à jour une flashcard existante.
   *
   * @param id identifiant de la flashcard à mettre à jour
   * @param flashcard nouvelles informations à appliquer
   * @return la flashcard mise à jour
   * @throws RuntimeException si la flashcard n'existe pas
   */
  public Flashcard updateFlashcard(final Long id, final Flashcard flashcard) {
    return flashcardRepository
        .findById(id)
        .map(
            f -> {
              f.setQuestion(flashcard.getQuestion());
              f.setAnswer(flashcard.getAnswer());
              f.setCategory(flashcard.getCategory());
              return flashcardRepository.save(f);
            })
        .orElseThrow(() -> new RuntimeException("Flashcard not found"));
  }

  /**
   * Supprime une flashcard selon son identifiant.
   *
   * @param id identifiant de la flashcard à supprimer
   */
  public void deleteFlashcard(final Long id) {
    flashcardRepository.deleteById(id);
  }
}
