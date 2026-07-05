# E-Commerce Shopping Cart - Developer Onboarding Guide

Welcome to the Microservices Shopping Cart Application! This document will help you understand the current architecture and get the project running locally.

## 🚀 Project Overview
This project is a microservices-based E-commerce application built with **Spring Boot 3**. It provides core shopping cart functionalities such as customer registration, authentication, adding items to a cart, removing items, and fetching the cart summary.

## 🏗️ Current Working Architecture
The application is composed of several microservices, all orchestrated via Docker Compose:

1. **`discovery-server` (Port 8761)**: Uses Netflix Eureka for service discovery. All other services register themselves here.
2. **`api-gateway` (Port 8080)**: The main entry point for all API requests. It handles routing to the appropriate backend microservices.
3. **`user-service`**: Manages customer registration and authentication, issuing JWT tokens for secured endpoints. It uses an in-memory **H2 database** for storage.
4. **`cart-command-service`**: Handles commands that mutate the cart state (adding or removing items). It uses an in-memory **H2 database** for persistence.
5. **`cart-query-service`**: Retrieves the shopping cart summary and its items. It integrates with a **Redis** cache container to provide fast read access.
6. **`redis` (Port 6379)**: Containerized Redis instance used by the query service for caching.

## 🛠️ Technology Stack
- **Backend Framework**: Java, Spring Boot 3.1.4
- **API Management**: Spring Cloud Gateway, Netflix Eureka
- **Persistence**: H2 (In-memory SQL Database)
- **Caching**: Redis
- **Security**: Spring Security & JWT
- **Containerization & Tooling**: Docker, Docker Compose, Maven, Makefile

## 💻 Local Development Setup

The project includes a `Makefile` that greatly simplifies the build and execution process. 

### Prerequisites
Make sure you have installed on your machine:
- Docker and Docker Compose
- Java (JDK 17+)
- Maven

### Running the Project
To build all microservices and start the Docker containers, run the following command in the root directory:
```bash
make all
```
*Note: This command runs `make build` (Maven compile/package) followed by `make up` (Docker Compose).*

The application gateway will be accessible at: `http://localhost:8080`

### Useful Makefile Commands
- `make build`: Compiles all microservices using Maven.
- `make up`: Starts all services via Docker Compose in detached mode.
- `make down` (or `make stop`): Stops all running services.
- `make logs`: Tails the logs of all running services.
- `make ps`: Shows the status of running containers.

## 🧪 Testing the API
The `/postman` directory contains JSON collections to help you test the API flow. 

1. Import the collections from the `postman` folder into your Postman workspace.
2. Ensure you have your Postman environment configured (typically targeting `localhost:8080`).
3. Follow the typical user journey:
   - `POST /users/register`: Register a new customer.
   - `POST /users/login`: Authenticate to receive a JWT.
   - Set the JWT as a Bearer Token for subsequent requests.
   - `POST /cart/add-item`: Add a product to your cart.
   - `POST /cart/remove-item`: Remove a product from your cart.
   - `GET /cart`: Retrieve your current cart summary.
