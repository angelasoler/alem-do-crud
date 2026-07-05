# API Gateway - JWT Authentication

## Overview

The API Gateway implements JWT (JSON Web Token) authentication as described in the project requirements. It serves as the front door to all microservices and handles authentication centrally.

## Authentication Flow

1. **Token Extraction**: The gateway looks for JWT tokens in the `Authorization` header with the format `Bearer <token>`
2. **Signature Validation**: Verifies the token signature using the shared secret key
3. **Expiration Check**: Validates that the token hasn't expired
4. **Token Routing**: Extracts user information and forwards it to downstream services

## Endpoints

### Public Endpoints (No Authentication Required)
- `GET /` - Welcome message
- `GET /health` - Health check
- `POST /users/register` - User registration
- `POST /users/login` - User login

### Protected Endpoints (JWT Required)
- `GET /users/**` - User management (except register/login)
- `POST /transactions/**` - Transaction operations
- `GET /balance/**` - Balance and query operations

## JWT Token Structure

The gateway expects JWT tokens with the following claims:
- `sub`: Username
- `user_id`: Unique user identifier
- `exp`: Expiration timestamp
- `iat`: Issued at timestamp

## Request Headers

### For Public Endpoints
No special headers required.

### For Protected Endpoints
```http
Authorization: Bearer <jwt-token>
```

## Response Headers Added by Gateway

When a valid JWT is provided, the gateway adds these headers to requests sent to downstream services:
- `X-User-Id`: The user ID extracted from the JWT
- `X-Username`: The username extracted from the JWT

## Error Responses

### 401 Unauthorized
```json
{
  "error": "Unauthorized",
  "message": "Missing or invalid Authorization header"
}
```

```json
{
  "error": "Unauthorized", 
  "message": "Invalid or expired token"
}
```

## Configuration

The JWT secret is configured in `application.properties`:
```properties
jwt.secret=mySecretKey123456789012345678901234567890
```

**Important**: In production, this secret should be:
1. Much more complex and random
2. Stored as an environment variable
3. Shared between User Service and API Gateway

## Service Discovery

The gateway uses Netflix Eureka for service discovery and routes requests to:
- `user-service` for `/users/**`
- `transaction-service` for `/transactions/**` 
- `query-service` for `/balance/**`

## Testing

To test the authentication:

1. Start the services: `docker-compose up`
2. Register a user: `POST /users/register`
3. Login to get a JWT: `POST /users/login`
4. Use the JWT for protected endpoints: `Authorization: Bearer <token>`

## Security Notes

- Tokens are validated on every request to protected endpoints
- Invalid or expired tokens result in immediate 401 responses
- User information is extracted and forwarded to downstream services
- The gateway acts as a security checkpoint, ensuring only authenticated requests reach the microservices
