package com.example.flashcards.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.flashcards.dto.FlashcardDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import org.junit.jupiter.api.Test;

class FlashcardMapperTest {

  @Test
  void toDto_null_returnsNull() {
    assertNull(FlashcardMapper.toDto(null));
  }

  @Test
  void toDto_happyPath_withCategory() {
    Category cat = new Category();
    cat.setId(3L);
    cat.setName("CatX");

    Flashcard f = new Flashcard();
    f.setId(11L);
    f.setQuestion("Q?");
    f.setAnswer("A");
    f.setCategory(cat);

    FlashcardDto dto = FlashcardMapper.toDto(f);

    assertEquals(11L, dto.getId());
    assertEquals("Q?", dto.getQuestion());
    assertEquals("A", dto.getAnswer());
    assertEquals(3L, dto.getCategoryId()); // branche non-null couverte
  }

  @Test
  void toDto_happyPath_withoutCategory() {
    Flashcard f = new Flashcard();
    f.setId(11L);
    f.setQuestion("Q?");
    f.setAnswer("A");
    f.setCategory(null);

    FlashcardDto dto = FlashcardMapper.toDto(f);

    assertEquals(11L, dto.getId());
    assertEquals("Q?", dto.getQuestion());
    assertEquals("A", dto.getAnswer());
    assertNull(dto.getCategoryId()); // branche null couverte
  }

  @Test
  void toEntity_null_returnsNull() {
    assertNull(FlashcardMapper.toEntity(null, null));
  }

  @Test
  void toEntity_withCategory() {
    Category cat = new Category();
    cat.setId(3L);
    cat.setName("CatX");

    FlashcardDto dto = new FlashcardDto();
    dto.setId(11L);
    dto.setQuestion("Q?");
    dto.setAnswer("A");

    Flashcard f = FlashcardMapper.toEntity(dto, cat);

    assertEquals(11L, f.getId());
    assertEquals("Q?", f.getQuestion());
    assertEquals("A", f.getAnswer());
    assertNotNull(f.getCategory());
    assertEquals(3L, f.getCategory().getId()); // branche if couverte
  }

  @Test
  void toEntity_withoutCategory() {
    FlashcardDto dto = new FlashcardDto();
    dto.setId(11L);
    dto.setQuestion("Q?");
    dto.setAnswer("A");

    Flashcard f = FlashcardMapper.toEntity(dto, null);

    assertEquals(11L, f.getId());
    assertEquals("Q?", f.getQuestion());
    assertEquals("A", f.getAnswer());
    assertNull(f.getCategory()); // branche else couverte
  }

  @Test
  void toDto_and_toEntity_roundtrip() {
    Category cat = new Category();
    cat.setId(3L);
    cat.setName("CatX");

    Flashcard f = new Flashcard();
    f.setId(11L);
    f.setQuestion("Q?");
    f.setAnswer("A");
    f.setCategory(cat);

    FlashcardDto dto = FlashcardMapper.toDto(f);
    Flashcard back = FlashcardMapper.toEntity(dto, cat);

    assertEquals(f.getId(), back.getId());
    assertEquals(f.getQuestion(), back.getQuestion());
    assertEquals(f.getAnswer(), back.getAnswer());

    Category returned = back.getCategory();
    assertNotNull(returned);
    assertEquals(cat.getId(), returned.getId());
    assertEquals(cat.getName(), returned.getName());
    assertNotSame(cat, returned, "copie défensive");
  }
}
