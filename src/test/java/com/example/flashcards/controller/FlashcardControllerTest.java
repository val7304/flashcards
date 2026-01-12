package com.example.flashcards.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flashcards.dto.FlashcardDto;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.service.CategoryService;
import com.example.flashcards.service.FlashcardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FlashcardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class FlashcardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FlashcardService flashcardService;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldGetAllFlashcards() throws Exception {
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Cat 1");

        Flashcard f = new Flashcard();
        f.setId(10L);
        f.setQuestion("Quelle est la capitale de la France ?");
        f.setAnswer("Paris");
        f.setCategory(cat);

        when(flashcardService.getAllFlashcards()).thenReturn(List.of(f));

        mockMvc
                .perform(get("/api/flashcards").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].question").value("Quelle est la capitale de la France ?"))
                .andExpect(jsonPath("$[0].answer").value("Paris"))
                .andExpect(jsonPath("$[0].categoryId").value(1));

        verify(flashcardService, times(1)).getAllFlashcards();
        verifyNoMoreInteractions(flashcardService);
    }

    @Test
    void searchByQuestion_returnsResults() throws Exception {
        when(flashcardService.searchByQuestion("capital"))
                .thenReturn(List.of(new FlashcardDto(1L, "Quelle est la capitale?", "Paris", 1L)));

        mockMvc
                .perform(get("/api/flashcards/search").param("question", "capital"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldCreateFlashcard() throws Exception {
        FlashcardDto newDto = new FlashcardDto();
        newDto.setQuestion("Quelle est la capitale de la France ?");
        newDto.setAnswer("Paris");
        newDto.setCategoryId(1L);

        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Cat 1");

        Flashcard saved = new Flashcard();
        saved.setId(100L);
        saved.setQuestion(newDto.getQuestion());
        saved.setAnswer(newDto.getAnswer());
        saved.setCategory(cat);

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(cat));
        when(flashcardService.createFlashcard(any(Flashcard.class))).thenReturn(saved);

        mockMvc
                .perform(
                        post("/api/flashcards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.question").value("Quelle est la capitale de la France ?"))
                .andExpect(jsonPath("$.answer").value("Paris"))
                .andExpect(jsonPath("$.categoryId").value(1));

        verify(categoryService, times(1)).getCategoryById(1L);
        verify(flashcardService, times(1)).createFlashcard(any(Flashcard.class));
        verifyNoMoreInteractions(categoryService, flashcardService);
    }
}
