package com.example.flashcards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entité représentant une carte mémoire (flashcard).
 * <p>
 * Une flashcard contient une question, une réponse et
 * est associée à une catégorie.
 * </p>
 */
@Entity
@Table(name = "flashcard")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {

    /**
     * Identifiant unique de la flashcard (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Question affichée sur la flashcard.
     */
    @Column(nullable = false)
    private String question;

    /**
     * Réponse associée à la question.
     */
    @Column(nullable = false)
    private String answer;

    /**
     * Catégorie à laquelle cette flashcard appartient.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
