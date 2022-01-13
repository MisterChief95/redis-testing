FROM openjdk:17.0.1-oracle
COPY ./target/redis-testing-*.jar /app.jar
RUN ["/usr/bin/java", "-jar", "/app.jar"]
