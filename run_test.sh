#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Function to display messages
print_message() {
  echo "=============================="
  echo "$1"
  echo "=============================="
}

# Maven clean
print_message "Cleaning the repo using Maven..."
mvn clean

# Maven build
print_message "Building the project with Maven..."
mvn install -DskipTests=true

# Run tests
print_message "Starting the test run..."
mvn test

# Final success message
print_message "Test run completed successfully!"