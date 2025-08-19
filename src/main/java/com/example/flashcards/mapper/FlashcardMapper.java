package com.example.flashcards.mapper;

import com.example.flashcards.dto.FlashcardDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.entity.Flashcard;

public class FlashcardMapper {

    public static FlashcardDTO toDTO(Flashcard flashcard) {
        if (flashcard == null) return null;
        return new FlashcardDTO(
                flashcard.getId(),
                flashcard.getQuestion(),
                flashcard.getAnswer(),
                flashcard.getCategory().getId()
        );
    }

    public static Flashcard toEntity(FlashcardDTO dto, Category category) {
        if (dto == null) return null;
        return new Flashcard(dto.getId(), dto.getQuestion(), dto.getAnswer(), category);
    }
}
