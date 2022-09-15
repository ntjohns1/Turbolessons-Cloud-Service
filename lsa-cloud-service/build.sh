#!/bin/bash

echo "Performing a clean Maven build"
./mvnw clean package -DskipTests=true

echo "Building the config server"
cd lsa-config-service
docker build --tag com-noslen-config-service .
cd ..

echo "Building the service registry"
cd service-registry
docker build --tag com-noslen-service-registry .
cd ..

echo "Building the authorization server"
cd authorization-server
docker build --tag com-noslen-authorization-server .
cd ..
