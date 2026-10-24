FROM gcr.io/distroless/java17-debian13:nonroot@sha256:1234331b9c3aab74deca18326bd1664d762935cd1fc44458b701d45a9611ca00

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
