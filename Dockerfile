FROM gcr.io/distroless/java17-debian13:nonroot@sha256:9bd5eb41f0a77adcbc10ff1c7ddcfddeada1796ca0dc827fe289269bd1339284

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
