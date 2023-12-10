FROM openjdk:17
LABEL maintainer = "Innocent udo"
WORKDIR /app
COPY Event-Booking-Sq16-0.0.1-SNAPSHOT.jar Event-Booking-Sq16.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "Event-Booking-Sq16.jar", "--spring.profiles.active=prod"]