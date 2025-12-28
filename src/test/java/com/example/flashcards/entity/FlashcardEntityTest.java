package com.example.flashcards.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlashcardEntityTest {

  private Flashcard flashcard;
  private Category category;

  @BeforeEach
  void setUp() {
    flashcard = new Flashcard();
    category = new Category();
    category.setId(10L);
    category.setName("Java");
  }

  @Test
  void getters_setters_basic() {
    // Couvre tous les getters/setters Lombok + ID génération
    flashcard.setId(1L);
    flashcard.setQuestion("Quelle est la capitale de la France ?");
    flashcard.setAnswer("Paris");

    assertEquals(1L, flashcard.getId());
    assertEquals("Quelle est la capitale de la France ?", flashcard.getQuestion());
    assertEquals("Paris", flashcard.getAnswer());
    assertNull(flashcard.getCategory());
  }

  @Test
  void relation_category_null() {
    // null case explicite (FetchType.LAZY)
    flashcard.setCategory(null);
    assertNull(flashcard.getCategory());
  }

  @Test
  void shouldAssociateCategory() {
    // @ManyToOne + accès lazy
    flashcard.setCategory(category);

    assertNotNull(flashcard.getCategory());
    assertEquals(10L, flashcard.getCategory().getId());
    assertEquals("Java", flashcard.getCategory().getName());
  }

  @Test
  void allArgsConstructor() {
    // @AllArgsConstructor (important pour Sonar)
    Flashcard f = new Flashcard(42L, "Q?", "A", category);

    assertEquals(42L, f.getId());
    assertEquals("Q?", f.getQuestion());
    assertEquals("A", f.getAnswer());
    assertEquals(category, f.getCategory());
  }

  @Test
  void noArgsConstructor() {
    // Couvre @NoArgsConstructor (par défaut dans tes tests)
    assertNotNull(new Flashcard());
  }
}
