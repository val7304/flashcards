FROM gcr.io/distroless/java17-debian13:nonroot@sha256:81d09cac6ec47f6a13c61a941557f95079213320f3ddbf9d353de9317669aab5

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
