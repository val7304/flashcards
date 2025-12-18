package com.example.flashcards;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class FlashcardsApplicationTest {

  @Test
  void testConstructor() {
    // Couvre le constructeur protégé
    assertDoesNotThrow(FlashcardsApplication::new);
  }

  @Test
  void testMainMethod() {
    // Couvre SpringApplication.run()
    assertDoesNotThrow(() -> FlashcardsApplication.main(new String[0]));
  }
}
