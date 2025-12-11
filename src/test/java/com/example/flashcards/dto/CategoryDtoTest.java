package com.example.flashcards.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryDtoTest {

  @Test
  void testAllGettersSetters() {
    CategoryDto dto = new CategoryDto();
    dto.setId(1L);
    dto.setName("Mathématiques");

    assertEquals(1L, dto.getId());
    assertEquals("Mathématiques", dto.getName());
  }

  @Test
  void testAllArgsConstructor() {
    CategoryDto dto = new CategoryDto(1L, "Informatique");
    assertEquals(1L, dto.getId());
    assertEquals("Informatique", dto.getName());
  }

  @Test
  void testEqualsHashCode() {
    CategoryDto dto1 = new CategoryDto(1L, "Math");
    CategoryDto dto2 = new CategoryDto(1L, "Math");
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
