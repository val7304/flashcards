package com.example.flashcards.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import org.junit.jupiter.api.Test;

class CategoryMapperTest {

  @Test
  void toDto_null_returnsNull() {
    assertNull(CategoryMapper.toDto(null));
  }

  @Test
  void toEntity_null_returnsNull() {
    assertNull(CategoryMapper.toEntity(null));
  }

  @Test
  void toDto_and_toEntity_roundtrip() {
    Category entity = new Category();
    entity.setId(7L);
    entity.setName("MyCat");

    CategoryDto dto = CategoryMapper.toDto(entity);
    assertNotNull(dto);
    assertEquals(7L, dto.getId());
    assertEquals("MyCat", dto.getName());

    Category back = CategoryMapper.toEntity(dto);
    assertNotNull(back);
    assertEquals(dto.getId(), back.getId());
    assertEquals(dto.getName(), back.getName());
  }
}
