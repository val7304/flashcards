FROM gcr.io/distroless/java17-debian13:nonroot@sha256:9a1ef1a994f57b543fb09b5a2d7ec5c8de638e310650d076998c9559eaeb6255

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
