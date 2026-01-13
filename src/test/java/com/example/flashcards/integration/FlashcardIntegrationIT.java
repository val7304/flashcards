package com.example.flashcards.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.flashcards.dto.CategoryDto;
import com.example.flashcards.dto.FlashcardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("it")
@AutoConfigureMockMvc
class FlashcardIntegrationIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testCRUDFLashcard() throws Exception {
    // Create Category for Flashcard
    CategoryDto category = new CategoryDto(null, "Math");
    String categoryJson = objectMapper.writeValueAsString(category);

    String categoryResponse =
        mockMvc
            .perform(
                post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(categoryJson))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    CategoryDto createdCategory = objectMapper.readValue(categoryResponse, CategoryDto.class);

    // Create Flashcard
    FlashcardDto newFlashcard = new FlashcardDto(null, "2+2?", "4", createdCategory.getId());
    String flashcardJson = objectMapper.writeValueAsString(newFlashcard);

    String flashcardResponse =
        mockMvc
            .perform(
                post("/api/flashcards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(flashcardJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.question").value("2+2?"))
            .andExpect(jsonPath("$.answer").value("4"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    FlashcardDto createdFlashcard = objectMapper.readValue(flashcardResponse, FlashcardDto.class);

    // Read Flashcard
    mockMvc
        .perform(get("/api/flashcards/" + createdFlashcard.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.question").value("2+2?"));

    // Update Flashcard
    FlashcardDto updatedFlashcard =
        new FlashcardDto(createdFlashcard.getId(), "3+3?", "6", createdCategory.getId());
    String updatedJson = objectMapper.writeValueAsString(updatedFlashcard);

    mockMvc
        .perform(
            put("/api/flashcards/" + createdFlashcard.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.question").value("3+3?"))
        .andExpect(jsonPath("$.answer").value("6"));

    // Delete Flashcard
    mockMvc
        .perform(delete("/api/flashcards/" + createdFlashcard.getId()))
        .andExpect(status().isOk());

    // Ensure Deleted
    mockMvc
        .perform(get("/api/flashcards/" + createdFlashcard.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  void testCreateFlashcardWithInvalidCategory() throws Exception {
    FlashcardDto invalid = new FlashcardDto(null, "Q?", "A", 999L); // ID inexistant
    mockMvc
        .perform(
            post("/api/flashcards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
        .andExpect(status().is4xxClientError()); // ou ce que tu veux
  }
}
