#!/bin/bash

# Parse command line arguments
RESTART_GATEWAY=false
while getopts "g" opt; do
  case $opt in
    g) RESTART_GATEWAY=true ;;
    *) ;;
  esac
done

# Start test environment
echo "Starting test environment..."
docker-compose -f docker-compose-test.yml up -d

# Restart API gateway if -g flag is provided
if [ "$RESTART_GATEWAY" = true ]; then
  echo "Restarting API gateway..."
  docker-compose -f docker-compose-test.yml restart api-gateway
fi
