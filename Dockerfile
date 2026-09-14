FROM gcr.io/distroless/java17-debian13:nonroot@sha256:6457ce026c1733d9693b65a4dbb45459d71ec1f5e48676f9d3877c99c63c35cc

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
