package com.example.flashcards.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressFBWarnings(
    value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "JPA entity - relations must expose references for ORM")

/**
 * Représente une catégorie de cartes mémoire (flashcards). Chaque catégorie contient une liste de
 * {@link Flashcard}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

  /** Identifiant unique de la catégorie. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Nom de la catégorie. */
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  /** Liste des flashcards associées à cette catégorie. */
  @OneToMany(
      mappedBy = "category",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Flashcard> flashcards = new ArrayList<>();

  /**
   * Constructeur sécurisé avec copie défensive pour éviter les NullPointerException.
   *
   * @param id identifiant de la catégorie
   * @param name nom de la catégorie
   * @param flashcards liste de flashcards (une copie défensive est créée)
   */
  public Category(Long id, String name, List<Flashcard> flashcards) {
    this.id = id;
    this.name = name;
    this.flashcards = (flashcards != null) ? new ArrayList<>(flashcards) : new ArrayList<>();
  }

  public List<Flashcard> getFlashcards() {
    return Collections.unmodifiableList(flashcards);
  }

  public void setFlashcards(List<Flashcard> flashcards) {
    this.flashcards = (flashcards != null) ? new ArrayList<>(flashcards) : new ArrayList<>();
  }

  /**
   * Retourne une copie non modifiable de la liste interne pour éviter l'exposition de la
   * représentation interne (EI_EXPOSE_REP).
   *
   * @return une vue non modifiable des flashcards
   */
  public void addFlashcard(Flashcard flashcard) {
    flashcards.add(flashcard);
    flashcard.setCategory(this);
  }

  /**
   * Définit la liste des flashcards en créant une copie défensive afin d'éviter toute modification
   * externe indésirable.
   *
   * @param flashcards la nouvelle liste de flashcards
   */
  public void removeFlashcard(Flashcard flashcard) {
    flashcards.remove(flashcard);
    flashcard.setCategory(null);
  }
}
