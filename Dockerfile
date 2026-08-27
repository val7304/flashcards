FROM gcr.io/distroless/java17-debian13:nonroot@sha256:c97caa04324a70ea594e82ac31d75cdcc3730845dfc528de38847dcbd52bb599

WORKDIR /app
COPY target/flashcards-*.jar app.jar

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
