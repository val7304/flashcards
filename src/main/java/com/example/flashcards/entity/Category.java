package com.example.flashcards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Entité représentant une catégorie de flashcards.
 * <p>
 * Une catégorie regroupe plusieurs flashcards et possède
 * un identifiant unique et un nom.
 * </p>
 */
@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Identifiant unique de la catégorie (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de la catégorie.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Liste des flashcards associées à cette catégorie.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Flashcard> flashcards;
}
