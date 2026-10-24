FROM gcr.io/distroless/java17-debian13:nonroot@sha256:bb551796de0958c414f26020d4fabd0fb3d23687819f7dbef1b713fac0872448

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
