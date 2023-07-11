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
COPY --from=build /home/app/target/event-service-0.0.1-SNAPSHOT.jar /usr/local/lib/event-service.jar
COPY /etc/letsencrypt/live/www.turbolessons.com/fullchain.p12 /etc/letsencrypt/live/www.turbolessons.com/fullchain.p12
EXPOSE 5001
ENTRYPOINT ["java","-jar","/usr/local/lib/event-service.jar"]