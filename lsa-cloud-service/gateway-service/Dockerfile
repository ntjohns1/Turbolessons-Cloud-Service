#
# Build stage
#
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/gateway-service-0.0.1-SNAPSHOT.jar /usr/local/lib/gateway-service.jar
EXPOSE 5001
ENTRYPOINT ["java","-jar","/usr/local/lib/gateway-service.jar"]