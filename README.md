# Car Dealer NL2SQL Backend

A Spring Boot backend application that converts natural language questions into SQL queries for a car dealership database using OpenAI and Spring AI.

## Features

- **Natural Language to SQL**: Convert user questions into SQL queries using OpenAI's GPT models
- **SQL Validation**: Ensures only safe SELECT queries are executed
- **REST API**: Simple POST endpoint for query processing
- **PostgreSQL Integration**: Executes queries against a PostgreSQL database
- **Production-Ready**: Clean, well-documented Java code with proper error handling

## Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 12+
- OpenAI API Key

## Setup

### 1. Clone and Navigate to Project

```bash
cd SQLMLProject
```

### 2. Set Environment Variables

Create a `.env` file or export the environment variable:

```bash
export OPENAI_API_KEY=your_openai_api_key_here
```

On Windows (PowerShell):
```powershell
$env:OPENAI_API_KEY="your_openai_api_key_here"
```

### 3. Configure PostgreSQL Database

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/car_dealer
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. Create Database Schema

Connect to PostgreSQL and run:

```sql
CREATE DATABASE car_dealer;

\c car_dealer;

CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    mileage INT NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE Customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    city VARCHAR(50)
);

CREATE TABLE Sale (
    id SERIAL PRIMARY KEY,
    car_id INT NOT NULL REFERENCES Car(id),
    customer_id INT NOT NULL REFERENCES Customer(id),
    sale_date DATE NOT NULL,
    sale_price DECIMAL(10, 2) NOT NULL
);

-- Sample data
INSERT INTO Car (make, model, year, price, mileage, status) VALUES
('Toyota', 'Corolla', 2022, 22000, 15000, 'available'),
('Toyota', 'Camry', 2021, 24000, 25000, 'available'),
('Honda', 'Civic', 2023, 25000, 5000, 'available'),
('Ford', 'Mustang', 2020, 35000, 40000, 'sold');

INSERT INTO Customer (name, email, phone, city) VALUES
('John Doe', 'john@example.com', '555-1234', 'New York'),
('Jane Smith', 'jane@example.com', '555-5678', 'Los Angeles');
```

### 5. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### POST /api/query

Converts a natural language question into SQL and executes it.

**Request:**
```json
{
  "question": "Show all Toyota cars under $25,000"
}
```

**Response:**
```json
{
  "sql": "SELECT * FROM Car WHERE make = 'Toyota' AND price < 25000;",
  "result": [
    {
      "id": 1,
      "make": "Toyota",
      "model": "Corolla",
      "year": 2022,
      "price": 22000,
      "mileage": 15000,
      "status": "available"
    },
    {
      "id": 2,
      "make": "Toyota",
      "model": "Camry",
      "year": 2021,
      "price": 24000,
      "mileage": 25000,
      "status": "available"
    }
  ],
  "error": null
}
```

### GET /api/health

Health check endpoint.

**Response:**
```
Service is running
```

## Example Queries

- "Show all available cars"
- "List cars with price less than $30,000"
- "Find all Toyota and Honda vehicles"
- "Show cars sold in the last year"
- "Get customers from New York"
- "List all sales with car details"

## Architecture

### Components

- **QueryController**: REST endpoint handler
- **QueryService**: Core business logic for NL2SQL conversion
- **OpenAIConfig**: Spring AI configuration for OpenAI integration
- **DTOs**: Request/Response data transfer objects

### Flow

1. User sends natural language question via REST API
2. QueryService builds a prompt with database schema context
3. OpenAI generates SQL query
4. SQL is validated (SELECT only, no dangerous operations)
5. Query is executed against PostgreSQL
6. Results are returned as JSON

## Security

- Only SELECT queries are allowed
- Dangerous operations (UPDATE, DELETE, INSERT, DROP, etc.) are rejected
- SQL validation prevents injection attacks
- API key is read from environment variables (never hardcoded)

## Configuration

Key properties in `application.properties`:

```properties
# OpenAI Model
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.3

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/car_dealer
```

## Troubleshooting

### OpenAI API Key Error
Ensure `OPENAI_API_KEY` environment variable is set correctly.

### Database Connection Error
Verify PostgreSQL is running and credentials in `application.properties` are correct.

### SQL Execution Error
Check that the database schema matches the expected tables (Car, Customer, Sale).

## Dependencies

- Spring Boot 3.5.7
- Spring AI 1.0.0-M1
- PostgreSQL Driver 42.7.1
- Lombok
- JUnit 5 (for testing)

