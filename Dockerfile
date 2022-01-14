FROM openjdk:17.0.1-oracle
COPY ./target/redis-testing-*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
