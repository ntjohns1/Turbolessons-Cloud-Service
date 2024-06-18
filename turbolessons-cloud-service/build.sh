#!/bin/bash

echo "Performing a clean Maven build"
./mvnw clean package -DskipTests=true

echo "Building the config server"
cd lsa-config-service
docker build -t noslenj/lsa-config-service:com-noslen-lsa-config-service .
cd ..

echo "Building the service registry"
cd service-registry
docker build -t noslenj/service-registry:com-noslen-service-registry .
cd ..

echo "Building admin-service"
cd admin-service
docker build -t noslenj/admin-service:com-noslen-admin-service .
cd ..

echo "Building email-service"
cd email-service
docker build -t noslenj/email-service:com-noslen-email-service .
cd ..

echo "Building event-service"
cd event-service
docker build -t noslenj/event-service:com-noslen-event-service .
cd ..

echo "Building lessondb"
cd lessondb
docker build -t noslenj/lessondb:com-noslen-lessondb .
cd ..

echo "Building message-service"
cd message-service
docker build -t noslenj/message-service:com-noslen-message-service .
cd ..

echo "Building payment-service"
cd payment-service
docker build -t noslenj/payment-service:com-noslen-payment-service .
cd ..

echo "Building video-service"
cd video-service
docker build -t noslenj/video-service:com-noslen-video-service .
cd ..

echo "Building api-gateway"
cd api-gateway
docker build -t noslenj/api-gateway:com-noslen-api-gateway .

