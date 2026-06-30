#
# Build stage
#
# syntax=docker/dockerfile:1
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY settings.xml /home/app/settings.xml
RUN --mount=type=secret,id=gh_token \
    GH_PACKAGES_TOKEN="$(cat /run/secrets/gh_token 2>/dev/null)" \
    mvn -f /home/app/pom.xml -s /home/app/settings.xml clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/admin-service-0.0.1-SNAPSHOT.jar /usr/local/lib/admin-service.jar
EXPOSE 5002
ENTRYPOINT ["java","-jar","/usr/local/lib/admin-service.jar"]