package com.example.flashcards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FlashcardsApplicationTest {

  @Test
  void contextLoads() {
    // Si le contexte démarre, le test passe
  }
}
