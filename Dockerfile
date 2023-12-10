FROM openjdk:17
LABEL maintainer = "Innocent udo"
WORKDIR /app
COPY Event-Management-System-0.0.1-SNAPSHOT.jar Event-Management-System-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "Event-Management-System-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]