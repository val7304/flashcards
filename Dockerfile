FROM gcr.io/distroless/java17-debian13:nonroot@sha256:1c6329f129ec1680322029528b12b5798e770658bee24d3a4854157992157255

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
