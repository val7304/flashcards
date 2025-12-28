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
  void toDto_happyPath() {
    Category entity = new Category();
    entity.setId(7L);
    entity.setName("MyCat");

    CategoryDto dto = CategoryMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals(7L, dto.getId());
    assertEquals("MyCat", dto.getName());
  }

  @Test
  void toEntity_happyPath() {
    CategoryDto dto = new CategoryDto();
    dto.setId(7L);
    dto.setName("MyCat");

    Category entity = CategoryMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(7L, entity.getId());
    assertEquals("MyCat", entity.getName());
  }

  // test roundtrip
  @Test
  void toDto_and_toEntity_roundtrip() {
    Category entity = new Category();
    entity.setId(7L);
    entity.setName("MyCat");

    CategoryDto dto = CategoryMapper.toDto(entity);
    Category back = CategoryMapper.toEntity(dto);

    assertEquals(entity.getId(), back.getId());
    assertEquals(entity.getName(), back.getName());
  }
}
