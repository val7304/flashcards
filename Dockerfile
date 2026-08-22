FROM gcr.io/distroless/java17-debian13:nonroot@sha256:2dcb2b21efab7e9250945985e0b0aab99ec7835db96a7bbc68edd1a0ad2e51ff

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
