package com.example.flashcards.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.mapper.CategoryMapper;
import com.example.flashcards.repository.CategoryRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryServiceTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllCategories() {
    Category cat1 = new Category(1L, "Math", null);
    Category cat2 = new Category(2L, "Science", null);

    when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

    List<Category> categories = categoryService.getAllCategories();
    List<CategoryDto> dtoList = categories.stream().map(CategoryMapper::toDto).toList();

    assertEquals(2, dtoList.size());
    assertEquals("Math", dtoList.get(0).getName());
  }

  @Test
  void testGetCategoryById() {
    Category category = new Category(1L, "History", null);
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    Optional<Category> found = categoryService.getCategoryById(1L);

    assertTrue(found.isPresent());
    CategoryDto dto = CategoryMapper.toDto(found.get());
    assertEquals("History", dto.getName());
  }

  @Test
  void testCreateCategory() {
    CategoryDto dto = new CategoryDto(null, "Histoire");
    Category entity = CategoryMapper.toEntity(dto);

    when(categoryRepository.save(any(Category.class)))
        .thenAnswer(
            inv -> {
              Category saved = inv.getArgument(0);
              saved.setId(1L);
              return saved;
            });

    Category saved = categoryService.createCategory(entity);
    CategoryDto savedDTO = CategoryMapper.toDto(saved);

    assertNotNull(savedDTO.getId());
    assertEquals("Histoire", savedDTO.getName());
  }

  @Test
  void testSearchByName() {
    Category cat = new Category(1L, "Math", null);
    when(categoryRepository.findByNameContainingIgnoreCase("ma")).thenReturn(List.of(cat));

    List<CategoryDto> result = categoryService.searchByName("ma");

    assertEquals(1, result.size());
    assertEquals("Math", result.get(0).getName());
    verify(categoryRepository).findByNameContainingIgnoreCase("ma");
  }

  @Test
  void testUpdateCategory_NotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, new Category()));
  }

  @Test
  void testUpdateCategory() {
    Category existing = new Category(1L, "Math", null);
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

    CategoryDto updateDTO = new CategoryDto(1L, "Physique");
    Category toUpdate = CategoryMapper.toEntity(updateDTO);

    when(categoryRepository.save(any(Category.class))).thenReturn(toUpdate);

    Category updated = categoryService.updateCategory(1L, toUpdate);
    CategoryDto updatedDTO = CategoryMapper.toDto(updated);

    assertEquals("Physique", updatedDTO.getName());
  }

  @Test
  void testDeleteCategory() {
    doNothing().when(categoryRepository).deleteById(1L);

    categoryService.deleteCategory(1L);

    verify(categoryRepository, times(1)).deleteById(1L);
  }
}
