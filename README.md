# Microservices Shopping Cart Application

This project is a microservices-based E-commerce Shopping Cart application. It is built using Spring Boot and follows the CQRS (Command Query Responsibility Segregation) pattern for high performance and scalability. The application is designed to be run and orchestrated with a single `docker-compose up` command.

## Architecture

```mermaid
graph TD
    subgraph Client [Client]
        A[Usuário];
    end

    subgraph API Gateway [API Gateway & Segurança]
        subgraph Validação JWT
            D -- Válido --> E[Token Válido];
            D -- Inválido --> F[401/403];
        end
        B(API Gateway)
    end
    
    subgraph Backend Services
        subgraph User Service
            direction LR
            C(User Service) --> C1(DB de Usuários);
        end
        subgraph CQRS [Arquitetura CQRS]
            subgraph Write [Command / Escrita]
                direction LR
                G[Cart Command Service] --> G1(DB do Carrinho);
            end

            subgraph Read [Query / Leitura]
                direction LR
                H[Query Service];
            end

            subgraph Cache Service [Cache Service]
                direction LR
                I[Redis];
            end
        end
    end

    H <--> I;
    E -- user ID do Header --> G;
    E -- user ID do Header --> H;
    G -- Atualização do Cache --> I;
    B <-- Login/Cadastro --> C

    A -- Requisição HTTP --> B;
    B -- Token JWT --> D{Validar Token?};
    D --> C;

    style A fill:#f8f9fa,stroke:#f8f9fa,color:#000
    style B fill:#e9ecef,stroke:#e9ecef,color:#000
    style C fill:#007bff,stroke:#007bff,color:#fff
    style C1 fill:#6c757d,stroke:#6c757d,color:#fff
    style D fill:#c7c7c7,stroke:#c7c7c7,color:#000
    style E fill:#d4edda,stroke:#d4edda,color:#000
    style F fill:#f8d7da,stroke:#f8d7da,color:#000
    style G fill:#28a745,stroke:#28a745,color:#fff
    style G1 fill:#6c757d,stroke:#6c757d,color:#fff
    style H fill:#dc3545,stroke:#dc3545,color:#fff
    style I fill:#ffc107,stroke:#ffc107,color:#000
```

The application is composed of the following microservices, each with a specific responsibility:

* **API Gateway**: A central entry point for all client requests. It handles authentication and routing to the appropriate backend service.

* **User Service**: Manages customer registration, login, and authentication with JWT (JSON Web Token).

* **Cart Command Service (Write Model)**: Manages all operations that change a user's cart state, such as adding or removing items.

* **Cart Query Service (Read Model)**: Provides a fast, read-only view of a user's current shopping cart summary and items.

## Technology Stack

* **Backend**: Java, Spring Boot 3.1.4
* **API Management**: Spring Cloud Gateway, Netflix Eureka
* **Persistence**: PostgreSQL (independent per-service databases for users and cart commands) and Redis (for fast read caching)
* **Containerization**: Docker and Docker Compose
* **Security**: Spring Security and JWT

## Endpoints

The following endpoints are available. The Postman collection provided can be used to test the complete flow.

### User Management

* `POST /users/register`: Registers a new customer.
    * **Body**: `{"fullName": "string", "documentNumber": "string", "login": "string", "password": "string"}`

* `POST /users/login`: Authenticates a customer and returns a JWT token.
    * **Body**: `{"login": "string", "password": "string"}`

### Cart Management (CQRS)

* `POST /cart/add-item`: Adds an item to the shopping cart. **Requires JWT.**
    * **Body**: `{"productId": "string", "quantity": 1, "price": 100.00}`

* `POST /cart/remove-item`: Removes an item from the shopping cart. **Requires JWT.**
    * **Body**: `{"productId": "string", "quantity": 1, "price": 100.00}`

* `GET /cart`: Retrieves the customer's current cart summary and added items. **Requires JWT.**

## Getting Started

1.  **Clone the repository**: `git clone <repository-url>`
2.  **Navigate to the project directory**: `cd <project-name>`
3.  **Run the application**: `make all` (Builds Maven and starts Docker Compose)
4.  The application will be accessible at `http://localhost:8080`.

## Testing

A Postman collection is included in the project (`postman/`) to help you test the authentication and shopping cart flows automatically.

## Contact Me 👋

Linkedin: [Angela Soler](https://www.linkedin.com/in/angela-soler-caro)

## Reference Links

* [Spring Cloud Projects](https://spring.io/projects/spring-cloud)
