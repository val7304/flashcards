FROM gcr.io/distroless/java17-debian13:nonroot@sha256:0be8a887d880a9a2b4734f3cef50c5c4f594d983db4dae5878eb6c218bfb3d7e

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
