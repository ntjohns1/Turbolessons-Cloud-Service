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
COPY --from=build /home/app/target/api-gateway-0.0.1-SNAPSHOT.jar /usr/local/lib/api-gateway.jar
COPY /etc/letsencrypt/live/www.turbolessons.com/fullchain.pem /etc/letsencrypt/live/www.turbolessons.com/fullchain.pem
EXPOSE 443
ENTRYPOINT ["java","-jar","/usr/local/lib/api-gateway.jar"]