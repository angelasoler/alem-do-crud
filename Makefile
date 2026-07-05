# Makefile for the E-Commerce Cart project

.PHONY: all build clean test up down stop restart logs ps help

# Default target executed when no specific target is provided
all: build up

# Build all microservices using Maven
# This command cleans the project and packages it.
build:
	@echo "Building all microservices..."
	mvn clean install

# Clean the Maven project
# This removes the target directory in each module.
clean:
	@echo "Cleaning the project..."
	mvn clean

# Run all tests in the project
test:
	@echo "Running tests..."
	mvn test

# Start all services with Docker Compose in detached mode
# The --build flag ensures images are rebuilt if the code changes.
up:
	@echo "Starting all services with Docker Compose..."
	docker compose up --build -d

# Stop all running services
down:
	@echo "Stopping all services..."
	docker compose down

# Alias for the "down" target
stop: down

# Restart all services
# This is a combination of stopping and starting the services.
restart: down up

# Tail the logs of all running services
# The -f flag follows the log output.
logs:
	@echo "Tailing logs..."
	docker compose logs -f

# Show the status of all running containers
ps:
	@echo "Showing container status..."
	docker compose ps

#Clean the old images
prune_images:
	docker image prune -f

# Display this help message
help:
	@echo "Available commands:"
	@echo "  make build    - Build all microservices with Maven."
	@echo "  make clean    - Clean the Maven project."
	@echo "  make test     - Run all tests."
	@echo "  make up       - Start all services with Docker Compose in detached mode."
	@echo "  make down     - Stop all services."
	@echo "  make stop     - Alias for 'down'."
	@echo "  make restart  - Restart all services."
	@echo "  make logs     - Tail the logs of all running services."
	@echo "  make ps       - Show the status of all running containers."
	@echo "  make help     - Display this help message."

