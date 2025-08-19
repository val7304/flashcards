package com.example.flashcards.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flashcard")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
