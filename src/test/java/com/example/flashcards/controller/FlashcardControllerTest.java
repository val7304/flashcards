package com.example.flashcards.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.flashcards.dto.FlashcardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FlashcardControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void shouldGetAllFlashcards() throws Exception {
    mockMvc
        .perform(get("/api/flashcards"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldCreateFlashcard() throws Exception {
    FlashcardDto newFlashcard = new FlashcardDto();
    newFlashcard.setQuestion("Quelle est la capitale de la France ?");
    newFlashcard.setAnswer("Paris");
    newFlashcard.setCategoryId(1L); // ⚠️ doit exister en base !

    mockMvc
        .perform(
            post("/api/flashcards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFlashcard)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.question").value("Quelle est la capitale de la France ?"))
        .andExpect(jsonPath("$.answer").value("Paris"));
  }
}
