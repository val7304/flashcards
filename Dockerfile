FROM gcr.io/distroless/java21-debian13:nonroot@sha256:b22857e939bd95abd69d9d747f0421a274f9f70855ecfc335f7d97f6eeef578d

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
