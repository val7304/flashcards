package com.example.flashcards.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CategoryService categoryService;

  @Test
  void shouldGetAllCategories() throws Exception {
    Category c = new Category();
    c.setId(1L);
    c.setName("Test Category");

    when(categoryService.getAllCategories()).thenReturn(List.of(c));

    mockMvc
        .perform(get("/api/categories").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Test Category"));

    verify(categoryService, times(1)).getAllCategories();
    verifyNoMoreInteractions(categoryService);
  }

  @Test
  void searchByName_returnsResults() throws Exception {
    when(categoryService.searchByName("algo"))
        .thenReturn(List.of(new CategoryDto(1L, "Algorithmes")));

    mockMvc
        .perform(get("/api/categories/search").param("name", "algo"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("Algorithmes"));
  }

  @Test
  void shouldCreateCategory() throws Exception {
    CategoryDto newDto = new CategoryDto();
    newDto.setName("New Category");

    Category saved = new Category();
    saved.setId(5L);
    saved.setName("New Category");

    when(categoryService.createCategory(any(Category.class))).thenReturn(saved);

    mockMvc
        .perform(
            post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.name").value("New Category"));

    verify(categoryService, times(1)).createCategory(any(Category.class));
    verifyNoMoreInteractions(categoryService);
  }
}
