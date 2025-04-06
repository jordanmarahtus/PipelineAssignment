FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/bookservice-0.0.1-SNAPSHOT.jar /app/bookservice.jar

EXPOSE 8080

CMD ["java", "-jar", "bookservice.war"]