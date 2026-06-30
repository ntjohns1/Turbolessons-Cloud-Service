#
# Build stage
#
# syntax=docker/dockerfile:1
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY settings.xml /home/app/settings.xml
# GH_PACKAGES_TOKEN (read:packages PAT) injected as a BuildKit secret to resolve
# com.turbolessons:keycloak-security-starter from GitHub Packages.
RUN --mount=type=secret,id=gh_token \
    GH_PACKAGES_TOKEN="$(cat /run/secrets/gh_token 2>/dev/null)" \
    mvn -f /home/app/pom.xml -s /home/app/settings.xml clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/payment-service-0.0.1-SNAPSHOT.jar /usr/local/lib/payment-service.jar
EXPOSE 5010
ENTRYPOINT ["java","-jar","/usr/local/lib/payment-service.jar"]