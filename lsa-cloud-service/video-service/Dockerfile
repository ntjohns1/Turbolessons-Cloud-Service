#
# Build stage
#
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/video-service-0.0.1-SNAPSHOT.jar /usr/local/lib/video-service.jar
COPY videoservice.env /usr/local/lib/videoservice.env
EXPOSE 5004

ENTRYPOINT ["sh", "-c", "java -jar /usr/local/lib/video-service.jar --spring.config.additional-location=file:/usr/local/lib/videoservice.env"]
