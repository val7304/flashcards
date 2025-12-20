package com.example.flashcards.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ApiDocs home() {
        String port = System.getProperty("server.port", "8080");
        String baseUrl = "http://localhost:" + port;

        return new ApiDocs(
                "🚀 Flashcards REST API - Production Ready",
                "UP | Port: " + port,
                baseUrl + "/api/categories", "List all categories",
                baseUrl + "/api/flashcards", "List all flashcards",
                baseUrl + "/actuator/health", "DB status + probes");
    }

    // DTO simple pour formatage propre
    public static class ApiDocs {
        public final String title, status, endpoint1, desc1, endpoint2, desc2, endpoint3, desc3;

        public ApiDocs(String title, String status, String e1, String d1, String e2, String d2, String e3, String d3) {
            this.title = title;
            this.status = status;
            this.endpoint1 = e1;
            this.desc1 = d1;
            this.endpoint2 = e2;
            this.desc2 = d2;
            this.endpoint3 = e3;
            this.desc3 = d3;
        }
    }
}
