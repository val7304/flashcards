<<<<<<< HEAD
FROM gcr.io/distroless/java17-debian13:nonroot@sha256:0be8a887d880a9a2b4734f3cef50c5c4f594d983db4dae5878eb6c218bfb3d7e
=======
FROM gcr.io/distroless/java17-debian13:nonroot@sha256:9a1ef1a994f57b543fb09b5a2d7ec5c8de638e310650d076998c9559eaeb6255
>>>>>>> staging

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
