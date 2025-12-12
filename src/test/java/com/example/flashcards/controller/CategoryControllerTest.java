package com.example.flashcards.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("GET /api/categories -> empty or one element")
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
  @DisplayName("GET /api/categories/{id} -> found")
  void getById_found() throws Exception {
    Category c = new Category();
    c.setId(2L);
    c.setName("Found");

    when(categoryService.getCategoryById(2L)).thenReturn(Optional.of(c));

    mockMvc
        .perform(get("/api/categories/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("Found"));
  }

  @Test
  @DisplayName("GET /api/categories/{id} -> not found")
  void getById_notFound() throws Exception {
    when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/categories/99")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/categories/search?name= -> returns DTO list")
  void searchByName_returnsResults() throws Exception {
    when(categoryService.searchByName("algo"))
        .thenReturn(List.of(new CategoryDto(1L, "Algorithmes")));

    mockMvc
        .perform(get("/api/categories/search").param("name", "algo"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Algorithmes"));
  }

  @Test
  @DisplayName("POST /api/categories -> create")
  void shouldCreateCategory() throws Exception {
    CategoryDto request = new CategoryDto();
    request.setName("New Category");

    Category saved = new Category();
    saved.setId(5L);
    saved.setName("New Category");

    // Service layer receives Entity (Category) — controller converts DTO -> Entity
    when(categoryService.createCategory(any(Category.class))).thenReturn(saved);

    mockMvc
        .perform(
            post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.name").value("New Category"));

    verify(categoryService, times(1)).createCategory(any(Category.class));
    verifyNoMoreInteractions(categoryService);
  }

  @Test
  @DisplayName("PUT /api/categories/{id} -> update")
  void shouldUpdateCategory() throws Exception {
    CategoryDto request = new CategoryDto();
    request.setName("Updated");

    Category updated = new Category();
    updated.setId(7L);
    updated.setName("Updated");

    when(categoryService.updateCategory(anyLong(), any(Category.class))).thenReturn(updated);

    mockMvc
        .perform(
            put("/api/categories/7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(7))
        .andExpect(jsonPath("$.name").value("Updated"));

    verify(categoryService, times(1)).updateCategory(anyLong(), any(Category.class));
  }

  @Test
  @DisplayName("DELETE /api/categories/{id} -> no content")
  void shouldDeleteCategory() throws Exception {
    mockMvc.perform(delete("/api/categories/10")).andExpect(status().isOk());
    verify(categoryService, times(1)).deleteCategory(10L);
  }
}
