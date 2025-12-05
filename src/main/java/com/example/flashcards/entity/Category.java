package com.example.flashcards.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une catégorie de cartes mémoire (flashcards).
 * Chaque catégorie contient une liste de {@link Flashcard}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    /** Identifiant unique de la catégorie. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de la catégorie. */
    private String name;

    /** Liste des flashcards associées à cette catégorie. */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flashcard> flashcards = new ArrayList<>();

    /**
     * Constructeur sécurisé avec copie défensive pour éviter les
     * NullPointerException.
     *
     * @param id         identifiant de la catégorie
     * @param name       nom de la catégorie
     * @param flashcards liste de flashcards (une copie défensive est créée)
     */
    public Category(Long id, String name, List<Flashcard> flashcards) {
        this.id = id;
        this.name = name;
        this.flashcards = (flashcards != null) ? new ArrayList<>(flashcards) : new ArrayList<>();
    }

    /**
     * Retourne une copie non modifiable de la liste interne pour éviter
     * l'exposition de la représentation interne (EI_EXPOSE_REP).
     *
     * @return une vue non modifiable des flashcards
     */
    public List<Flashcard> getFlashcards() {
        return Collections.unmodifiableList(new ArrayList<>(flashcards));
    }

    /**
     * Définit la liste des flashcards en créant une copie défensive
     * afin d'éviter toute modification externe indésirable.
     *
     * @param flashcards la nouvelle liste de flashcards
     */
    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = (flashcards != null) ? new ArrayList<>(flashcards) : new ArrayList<>();
    }
}
