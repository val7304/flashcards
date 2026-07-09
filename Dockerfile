FROM gcr.io/distroless/java17-debian13:nonroot@sha256:c12a27779035995d1acfff43dcda4f6e7654259d9690c6d1d5bd3cde86e85133

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
