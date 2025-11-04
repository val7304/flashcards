package com.example.flashcards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une carte mémoire (flashcard).
 * Chaque flashcard appartient à une {@link Category}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flashcard {

    /** Identifiant unique de la flashcard. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Question de la flashcard. */
    private String question;

    /** Réponse de la flashcard. */
    private String answer;

    /** Catégorie associée à la flashcard. */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Constructeur sécurisé avec gestion de la nullité.
     *
     * @param id       identifiant de la flashcard
     * @param question question posée
     * @param answer   réponse attendue
     * @param category catégorie associée (une copie défensive est créée si
     *                 nécessaire)
     */
    public Flashcard(Long id, String question, String answer, Category category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = (category != null)
                ? new Category(category.getId(), category.getName(), category.getFlashcards())
                : new Category();
    }

    /**
     * Retourne une copie défensive de la catégorie associée
     * pour éviter l’exposition interne (EI_EXPOSE_REP).
     *
     * @return une copie de la catégorie associée
     */
    public Category getCategory() {
        return (category != null)
                ? new Category(category.getId(), category.getName(), category.getFlashcards())
                : null;
    }

    /**
     * Définit la catégorie associée en créant une copie défensive
     * afin d’éviter toute modification externe indésirable.
     *
     * @param category la nouvelle catégorie
     */
    public void setCategory(Category category) {
        this.category = (category != null)
                ? new Category(category.getId(), category.getName(), category.getFlashcards())
                : null;
    }
}
