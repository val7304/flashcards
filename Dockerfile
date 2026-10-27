FROM gcr.io/distroless/java17-debian13:nonroot@sha256:11e8cf459b4de4e46248952ff1a5121077e757bd4cb3f21c6e74ea47215cdb62

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
