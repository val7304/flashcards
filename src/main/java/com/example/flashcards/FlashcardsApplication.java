package com.example.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;

/**
 * Classe principale de l'application Flashcards.
 *
 * <p>Cette classe lance l'application Spring Boot.
 */
@SpringBootApplication(
    exclude = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class,
      UserDetailsServiceAutoConfiguration.class
    })
public class FlashcardsApplication {

  /**
   * Constructeur explicite requis par Checkstyle. Corrige la règle
   * HideUtilityClassConstructorCheck.
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
