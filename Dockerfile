FROM gcr.io/distroless/java17-debian13:nonroot@sha256:c163251b62cd635e85d19014ce7b6bbf769146b576c69fc220a05c3d3c99d94b

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
