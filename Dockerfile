FROM gcr.io/distroless/java17-debian13:nonroot@sha256:90003c9403e8243b5da7459fab0729fc2e5507f08c710c3bc8b0df207f26f050

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
