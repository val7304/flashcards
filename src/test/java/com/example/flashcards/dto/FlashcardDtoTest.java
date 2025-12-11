package com.example.flashcards.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FlashcardDtoTest {

  @Test
  void testAllGettersSetters() {
    FlashcardDto dto = new FlashcardDto();
    dto.setId(1L);
    dto.setQuestion("Quelle est la capitale ?");
    dto.setAnswer("Paris");
    dto.setCategoryId(2L);

    assertEquals(1L, dto.getId());
    assertEquals("Quelle est la capitale ?", dto.getQuestion());
    assertEquals("Paris", dto.getAnswer());
    assertEquals(2L, dto.getCategoryId());
  }

  @Test
  void testAllArgsConstructor() {
    FlashcardDto dto = new FlashcardDto(1L, "Q", "A", 2L);
    assertEquals(1L, dto.getId());
    assertEquals("Q", dto.getQuestion());
    assertEquals("A", dto.getAnswer());
    assertEquals(2L, dto.getCategoryId());
  }

  @Test
  void testEqualsHashCode() {
    FlashcardDto dto1 = new FlashcardDto(1L, "Q", "A", 2L);
    FlashcardDto dto2 = new FlashcardDto(1L, "Q", "A", 2L);
    assertEquals(dto1, dto2);
    assertEquals(dto1.hashCode(), dto2.hashCode());
  }

  @Test
  void testNullValues() {
    FlashcardDto dto = new FlashcardDto();
    dto.setId(null);
    dto.setQuestion(null);
    dto.setAnswer(null);
    dto.setCategoryId(null);
    assertNull(dto.getId()); // etc.
  }
}
