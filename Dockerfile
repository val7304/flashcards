FROM gcr.io/distroless/java21-debian13:nonroot@sha256:d978eefd303c59c42f382aef811a0e41f2c68a6e5c319a23769cd3d1d5bf8e3c

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
