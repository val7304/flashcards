package com.example.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Flashcards.
 * <p>
 * Cette classe lance l'application Spring Boot.
 * </p>
 */
@SpringBootApplication
public class FlashcardsApplication {

    /**
     * Constructeur explicite requis par Checkstyle.
     * Corrige la règle HideUtilityClassConstructorCheck.
     */
    protected FlashcardsApplication() {
        // Constructeur vide intentionnellement.
    }

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(final String[] args) {
        SpringApplication.run(FlashcardsApplication.class, args);
    }
}
