package com.example.flashcards.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryEntityTest {

  private Category category;
  private Flashcard flashcard1;
  private Flashcard flashcard2;

  @BeforeEach
  void setUp() {
    category = new Category();
    flashcard1 = new Flashcard();
    flashcard1.setId(1L);
    flashcard2 = new Flashcard();
    flashcard2.setId(2L);
  }

  @Test
  void getters_setters_basic() {
    // Couvre setters/getters + génération ID
    category.setId(1L);
    category.setName("Math");

    assertEquals(1L, category.getId());
    assertEquals("Math", category.getName());
    assertNotNull(category.getFlashcards());
    assertTrue(category.getFlashcards().isEmpty());
    assertNull(category.getCategory()); // relation parent par défaut null
  }

  @Test
  void relation_parent_category() {
    // Couvre @ManyToOne lazy + getter/setter custom
    Category parent = new Category(10L, "Parent", null);
    category.setCategory(parent);
    assertEquals(parent, category.getCategory());
  }

  @Test
  void addFlashcard_single() {
    // Couvre addFlashcard() + bidirectionnel
    category.addFlashcard(flashcard1);

    assertEquals(1, category.getFlashcards().size());
    assertEquals(category, flashcard1.getCategory()); // bidirectionnel
  }

  @Test
  void addFlashcard_multiple() {
    // Couvre ArrayList.add() + orphanRemoval implicit
    category.addFlashcard(flashcard1);
    category.addFlashcard(flashcard2);

    assertEquals(2, category.getFlashcards().size());
    assertTrue(category.getFlashcards().contains(flashcard1));
    assertTrue(category.getFlashcards().contains(flashcard2));
  }

  @Test
  void removeFlashcard() {
    // Couvre removeFlashcard() + bidirectionnel nullification
    category.addFlashcard(flashcard1);
    category.removeFlashcard(flashcard1);

    assertEquals(0, category.getFlashcards().size());
    assertNull(flashcard1.getCategory());
  }

  // Tes tests existants (copie défensive) - on les garde
  @Test
  void defensiveCopy_setFlashcards_and_getFlashcards() {
    category.setId(1L);
    category.setName("C");

    List<Flashcard> external = new ArrayList<>();
    external.add(flashcard1);

    category.setFlashcards(external);
    external.add(flashcard2); // modifie externe

    List<Flashcard> internal = category.getFlashcards();
    assertEquals(1, internal.size()); // interne non affecté
    assertThrows(UnsupportedOperationException.class, () -> internal.add(flashcard2));
  }

  @Test
  void constructor_defensive_copy_with_null() {
    Category c = new Category(2L, "Name", null);
    assertNotNull(c.getFlashcards());
    assertEquals(0, c.getFlashcards().size());
  }
}
