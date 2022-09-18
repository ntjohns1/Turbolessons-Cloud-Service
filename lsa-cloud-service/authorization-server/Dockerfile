#
# Build stage
#
FROM maven AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true

#
# Package stage
#
FROM adoptopenjdk
COPY --from=build /home/app/target/authorization-server-0.1.0-SNAPSHOT.jar /usr/local/lib/authorization-server.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/authorization-server.jar"]