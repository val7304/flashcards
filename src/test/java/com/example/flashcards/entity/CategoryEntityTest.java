package com.example.flashcards.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryEntityTest {

  @Test
  void defensiveCopy_setFlashcards_and_getFlashcards() {
    Category category = new Category();
    category.setId(1L);
    category.setName("C");

    List<Flashcard> external = new ArrayList<>();
    Flashcard f1 = new Flashcard();
    f1.setId(1L);
    external.add(f1);

    // setFlashcards should copy the list
    category.setFlashcards(external);

    // modify original external list
    external.add(new Flashcard());

    // internal list shouldn't have been affected by external modification
    List<Flashcard> internal = category.getFlashcards();
    assertEquals(1, internal.size());

    // returned list must be unmodifiable
    Flashcard flashcard = new Flashcard();

    assertThrows(UnsupportedOperationException.class, () -> internal.add(flashcard));
  }

  @Test
  void constructor_defensive_copy_with_null() {
    Category c = new Category(2L, "Name", null);
    assertNotNull(c.getFlashcards());
    assertEquals(0, c.getFlashcards().size());
  }
}
