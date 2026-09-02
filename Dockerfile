FROM gcr.io/distroless/java17-debian13:nonroot@sha256:cc22e72aa8540b9b87f056a30319c62fa6ebd98d6463f364b162ef9723e1e695

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
