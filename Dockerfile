FROM eclipse-temurin:17-jre

RUN useradd -r -u 1001 appuser
WORKDIR /app
COPY target/flashcards-*.jar app.jar
RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
