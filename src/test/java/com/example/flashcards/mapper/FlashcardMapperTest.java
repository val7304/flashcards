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
  void toEntity_null_returnsNull() {
    assertNull(FlashcardMapper.toEntity(null, null));
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
    assertNotNull(dto);
    assertEquals(11L, dto.getId());
    assertEquals("Q?", dto.getQuestion());
    assertEquals("A", dto.getAnswer());
    assertEquals(3L, dto.getCategoryId());

    Flashcard back = FlashcardMapper.toEntity(dto, cat);
    assertNotNull(back);
    assertEquals(dto.getId(), back.getId());
    assertEquals(dto.getQuestion(), back.getQuestion());
    assertEquals(dto.getAnswer(), back.getAnswer());

    // Le Flashcard.getCategory retourne une copie défensive : vérifier les champs
    // et non l'identité
    Category returned = back.getCategory();
    assertNotNull(returned);
    assertEquals(cat.getId(), returned.getId());
    assertEquals(cat.getName(), returned.getName());
    assertNotSame(
        cat, returned, "getCategory() doit retourner une copie défensive, pas la même instance");
  }
}
