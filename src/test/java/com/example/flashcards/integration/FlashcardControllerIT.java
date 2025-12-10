package com.example.flashcards.integration;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.dto.FlashcardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlashcardControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateAndGetFlashcard() throws Exception {
    // 1. Créer une catégorie d'abord
    CategoryDto category = new CategoryDto(null, "ITCategory");
    String categoryJson = objectMapper.writeValueAsString(category);

    String categoryResponse = mockMvc
        .perform(
            post("/api/categories").contentType(MediaType.APPLICATION_JSON).content(categoryJson))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    CategoryDto savedCategory = objectMapper.readValue(categoryResponse, CategoryDto.class);

    // 2. Créer une flashcard liée à cette catégorie
    FlashcardDto flashcard =
        new FlashcardDto(null, "What is Java?", "A programming language", savedCategory.getId());
    String flashcardJson = objectMapper.writeValueAsString(flashcard);

    mockMvc
        .perform(
            post("/api/flashcards").contentType(MediaType.APPLICATION_JSON).content(flashcardJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.question").value("What is Java?"))
        .andExpect(jsonPath("$.answer").value("A programming language"))
        .andExpect(jsonPath("$.categoryId").value(savedCategory.getId()));

    // 3. Vérifier qu'elle est récupérable
    mockMvc.perform(get("/api/flashcards")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0].question").value("What is Java?"));
  }
}
