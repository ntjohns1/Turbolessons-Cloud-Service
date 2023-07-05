#!/bin/bash

echo "Performing a clean Maven build"
./mvnw clean package -DskipTests=true

echo "Building the config server"
cd lsa-config-service
docker build --tag com-noslen-lsa-config-service .
cd ..

echo "Building the service registry"
cd service-registry
docker build --tag com-noslen-service-registry .
cd ..

echo "Building admin-service"
cd admin-service
docker build --tag com-noslen-admin-service .
cd ..

echo "Building email-service"
cd email-service
docker build --tag com-noslen-email-service .
cd ..

echo "Building event-service"
cd event-service
docker build --tag com-noslen-event-service .
cd ..

echo "Building message-service"
cd message-service
docker build --tag com-noslen-message-service .
cd ..

echo "Building video-service"
cd video-service
docker build --tag com-noslen-video-service .
cd ..

echo "Building api-gateway"
cd api-gateway
docker build --tag com-noslen-api-gateway .
cd ..
