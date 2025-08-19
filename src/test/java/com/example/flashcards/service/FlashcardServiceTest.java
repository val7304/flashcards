package com.example.flashcards.service;

import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.repository.FlashcardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @InjectMocks
    private FlashcardService flashcardService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category(1L, "Science", null);
    }

    @Test
    void testGetAllFlashcards() {
        Flashcard f1 = new Flashcard(1L, "Q1", "A1", category);
        Flashcard f2 = new Flashcard(2L, "Q2", "A2", category);

        when(flashcardRepository.findAll()).thenReturn(Arrays.asList(f1, f2));

        List<Flashcard> flashcards = flashcardService.getAllFlashcards();
        List<FlashcardDTO> dtoList = flashcards.stream()
                .map(FlashcardMapper::toDTO)
                .toList();

        assertEquals(2, dtoList.size());
        assertEquals("Q1", dtoList.get(0).getQuestion());
    }

    @Test
    void testGetFlashcardById() {
        Flashcard flashcard = new Flashcard(1L, "What?", "Answer", category);
        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        Optional<Flashcard> found = flashcardService.getFlashcardById(1L);

        assertTrue(found.isPresent());
        FlashcardDTO dto = FlashcardMapper.toDTO(found.get());
        assertEquals("What?", dto.getQuestion());
    }

    @Test
    void testCreateFlashcard() {
        FlashcardDTO dto = new FlashcardDTO(null, "Q?", "A", category.getId());
        Flashcard entity = FlashcardMapper.toEntity(dto, category);

        when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(inv -> {
            Flashcard saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Flashcard saved = flashcardService.createFlashcard(entity);
        FlashcardDTO savedDTO = FlashcardMapper.toDTO(saved);

        assertNotNull(savedDTO.getId());
        assertEquals("Q?", savedDTO.getQuestion());
    }

    @Test
    void testUpdateFlashcard() {
        Flashcard existing = new Flashcard(1L, "Old Q", "Old A", category);
        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(existing));

        FlashcardDTO dto = new FlashcardDTO(1L, "New Q", "New A", category.getId());
        Flashcard toUpdate = FlashcardMapper.toEntity(dto, category);

        when(flashcardRepository.save(any(Flashcard.class))).thenReturn(toUpdate);

        Flashcard updated = flashcardService.updateFlashcard(1L, toUpdate);
        FlashcardDTO updatedDTO = FlashcardMapper.toDTO(updated);

        assertEquals("New Q", updatedDTO.getQuestion());
        assertEquals("New A", updatedDTO.getAnswer());
    }

    @Test
    void testDeleteFlashcard() {
        doNothing().when(flashcardRepository).deleteById(1L);

        flashcardService.deleteFlashcard(1L);

        verify(flashcardRepository, times(1)).deleteById(1L);
    }
}
