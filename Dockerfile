FROM gcr.io/distroless/java17-debian13:nonroot@sha256:b0e67f7fa5649297e655e37b2cd67471d7d38c2f8617790e9d8e8eab78ed6bcb

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
