package com.example.flashcards.controller;

import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.service.CategoryService;
import com.example.flashcards.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final CategoryService categoryService;

    @GetMapping
    public List<FlashcardDTO> getAll() {
        return flashcardService.getAllFlashcards()
                .stream()
                .map(FlashcardMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardDTO> getById(@PathVariable Long id) {
        return flashcardService.getFlashcardById(id)
                .map(FlashcardMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<FlashcardDTO>> searchByQuestion(
            @RequestParam("question") String question) {
        return ResponseEntity.ok(flashcardService.searchByQuestion(question));
    }

    @PostMapping
    public FlashcardDTO create(@RequestBody FlashcardDTO dto) {
        Category category = categoryService.getCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Flashcard saved = flashcardService.createFlashcard(FlashcardMapper.toEntity(dto, category));
        return FlashcardMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public FlashcardDTO update(@PathVariable Long id, @RequestBody FlashcardDTO dto) {
        Category category = categoryService.getCategoryById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Flashcard updated = flashcardService.updateFlashcard(id, FlashcardMapper.toEntity(dto, category));
        return FlashcardMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
    }
}
