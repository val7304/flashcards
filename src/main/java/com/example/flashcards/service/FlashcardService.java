package com.example.flashcards.service;

import com.example.flashcards.entity.Flashcard;
import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.mapper.FlashcardMapper;
import com.example.flashcards.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;

    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    public Optional<Flashcard> getFlashcardById(Long id) {
        return flashcardRepository.findById(id);
    }

    public List<FlashcardDTO> searchByQuestion(String question) {
        return flashcardRepository.findByQuestionContainingIgnoreCase(question)
                .stream()
                .map(FlashcardMapper::toDTO)
                .toList();
    }

    public Flashcard createFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public Flashcard updateFlashcard(Long id, Flashcard flashcard) {
        return flashcardRepository.findById(id).map(f -> {
            f.setQuestion(flashcard.getQuestion());
            f.setAnswer(flashcard.getAnswer());
            f.setCategory(flashcard.getCategory());
            return flashcardRepository.save(f);
        }).orElseThrow(() -> new RuntimeException("Flashcard not found"));
    }

    public void deleteFlashcard(Long id) {
        flashcardRepository.deleteById(id);
    }
}
