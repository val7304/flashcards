package com.example.flashcards.integration;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.flashcards.dto.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class CategoryIntegrationIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testCRUDCategory() throws Exception {
    // Create Category
    CategoryDto newCategory = new CategoryDto(null, "Science");
    String categoryJson = objectMapper.writeValueAsString(newCategory);

    String response =
        mockMvc
            .perform(
                post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(categoryJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("Science"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    CategoryDto created = objectMapper.readValue(response, CategoryDto.class);

    // Read Category
    mockMvc
        .perform(get("/api/categories/" + created.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Science"));

    // 3. Update Category
    CategoryDto updatedCategory = new CategoryDto(created.getId(), "Biology");
    String updatedJson = objectMapper.writeValueAsString(updatedCategory);

    mockMvc
        .perform(
            put("/api/categories/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Biology"));

    // Delete Category
    mockMvc.perform(delete("/api/categories/" + created.getId())).andExpect(status().isOk());

    // Ensure Deleted
    mockMvc.perform(get("/api/categories/" + created.getId())).andExpect(status().isNotFound());
  }

  @Test
  void testSearchByName() throws Exception {
    // Create categorie (without retrieved response)
    mockMvc
        .perform(
            post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CategoryDto(null, "Science"))))
        .andExpect(status().isOk());

    // Check search
    mockMvc
        .perform(get("/api/categories/search").param("name", "Scie"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name", hasItem("Science")));
  }
}