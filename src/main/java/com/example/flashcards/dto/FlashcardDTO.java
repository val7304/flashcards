package com.example.flashcards.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardDTO {
    private Long id;
    private String question;
    private String answer;
    private Long categoryId;
}
