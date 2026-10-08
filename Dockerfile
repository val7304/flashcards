FROM gcr.io/distroless/java17-debian13:nonroot@sha256:b318d135426b282db46c3e2601edeb41eb7b157d9ab75d2f9499743180cddf0e

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
