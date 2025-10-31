package com.example.flashcards.integration;

import com.example.flashcards.dto.CategoryDto;
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
class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCRUDCategory() throws Exception {
        // 1. Create Category
        CategoryDto newCategory = new CategoryDto(null, "Science");
        String categoryJson = objectMapper.writeValueAsString(newCategory);

        String response = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Science"))
                .andReturn().getResponse().getContentAsString();

        CategoryDto created = objectMapper.readValue(response, CategoryDto.class);

        // 2. Read Category
        mockMvc.perform(get("/api/categories/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Science"));

        // 3. Update Category
        CategoryDto updatedCategory = new CategoryDto(created.getId(), "Biology");
        String updatedJson = objectMapper.writeValueAsString(updatedCategory);

        mockMvc.perform(put("/api/categories/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Biology"));

        // 4. Delete Category
        mockMvc.perform(delete("/api/categories/" + created.getId()))
                .andExpect(status().isOk());

        // 5. Ensure Deleted
        mockMvc.perform(get("/api/categories/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
