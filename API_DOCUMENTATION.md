# Car Dealer NL2SQL Backend - API Documentation

## Base URL
```
http://localhost:8080/api
```

---

## 1. Natural Language to SQL Endpoint

### POST `/query`
Convert natural language questions into SQL queries and execute them.

**Request:**
```json
{
  "question": "Show me all available Toyota cars under $25,000"
}
```

**Response (Success):**
```json
{
  "sql": "SELECT * FROM Car WHERE make = 'Toyota' AND price < 25000 AND status = 'available'",
  "result": [
    {
      "id": 1,
      "make": "Toyota",
      "model": "Corolla",
      "year": 2023,
      "price": 22500.00,
      "mileage": 5000,
      "status": "available"
    }
  ],
  "error": null
}
```

**Response (Error):**
```json
{
  "sql": "DELETE FROM Car",
  "result": null,
  "error": "Invalid SQL: Only SELECT queries are allowed"
}
```

---

## 2. Dashboard Endpoints

### GET `/dashboard/stats`
Get comprehensive dashboard statistics including totals, revenue, and inventory breakdown.

**Response:**
```json
{
  "totalCars": 37,
  "availableCars": 28,
  "soldCars": 7,
  "reservedCars": 2,
  "totalRevenue": 450000.00,
  "averageSalePrice": 22500.00,
  "totalSales": 20,
  "inventoryByMake": {
    "Toyota": 6,
    "Honda": 6,
    "Ford": 6,
    "BMW": 4,
    "Mercedes-Benz": 3,
    "Chevrolet": 3,
    "Hyundai": 3,
    "Kia": 3
  },
  "topSales": [
    {
      "id": 1,
      "carMake": "Toyota",
      "carModel": "Camry",
      "customerName": "John Doe",
      "saleDate": "2024-01-15",
      "salePrice": 23500.00
    }
  ]
}
```

---

## 3. Car Endpoints

### GET `/dashboard/cars`
Get all cars with optional filtering and pagination.

**Query Parameters:**
- `status` (optional): Filter by status (available, sold, reserved)
- `make` (optional): Filter by make
- `minPrice` (optional): Filter by minimum price
- `maxPrice` (optional): Filter by maximum price
- `page` (default: 0): Page number (0-indexed)
- `size` (default: 20): Page size
- `sortBy` (default: id): Field to sort by
- `direction` (default: ASC): Sort direction (ASC or DESC)

**Example Request:**
```
GET /dashboard/cars?status=available&page=0&size=10&sortBy=price&direction=ASC
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "make": "Toyota",
      "model": "Corolla",
      "year": 2023,
      "price": 22500.00,
      "mileage": 5000,
      "status": "available"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 3,
  "totalElements": 28,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

### GET `/dashboard/cars/available`
Get available cars only (shortcut endpoint).

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Example Request:**
```
GET /dashboard/cars/available?page=0&size=10
```

### GET `/dashboard/cars/make/{make}`
Get cars by specific make.

**Path Parameters:**
- `make`: Car manufacturer (e.g., Toyota, Honda, Ford)

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Example Request:**
```
GET /dashboard/cars/make/Toyota?page=0&size=10
```

### GET `/dashboard/cars/price-range`
Get cars within a price range.

**Query Parameters:**
- `minPrice` (required): Minimum price
- `maxPrice` (required): Maximum price
- `page` (default: 0)
- `size` (default: 20)

**Example Request:**
```
GET /dashboard/cars/price-range?minPrice=20000&maxPrice=30000&page=0&size=10
```

---

## 4. Sales Endpoints

### GET `/dashboard/sales`
Get all sales with pagination.

**Query Parameters:**
- `page` (default: 0)
- `size` (default: 20)

**Example Request:**
```
GET /dashboard/sales?page=0&size=10
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "car": {
        "id": 4,
        "make": "Toyota",
        "model": "Camry",
        "year": 2021,
        "price": 24000.00,
        "mileage": 35000,
        "status": "sold"
      },
      "customer": {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@email.com",
        "phone": "555-0101",
        "city": "New York"
      },
      "saleDate": "2024-01-15",
      "salePrice": 23500.00
    }
  ],
  "pageable": {...},
  "totalPages": 2,
  "totalElements": 20,
  "last": false,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

---

## 5. Customer Endpoints

### GET `/dashboard/customers`
Get all customers with optional filtering and pagination.

**Query Parameters:**
- `city` (optional): Filter by city
- `name` (optional): Filter by name (partial match, case-insensitive)
- `page` (default: 0)
- `size` (default: 20)

**Example Request:**
```
GET /dashboard/customers?city=New York&page=0&size=10
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@email.com",
      "phone": "555-0101",
      "city": "New York"
    }
  ],
  "pageable": {...},
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

---

## 6. Health Check

### GET `/health`
Check if the service is running.

**Response:**
```
Service is running
```

---

## Example Frontend Usage

### JavaScript/React Example

```javascript
// Get dashboard statistics
const fetchDashboardStats = async () => {
  const response = await fetch('http://localhost:8080/api/dashboard/stats');
  const data = await response.json();
  console.log(data);
};

// Get available cars
const fetchAvailableCars = async (page = 0, size = 10) => {
  const response = await fetch(
    `http://localhost:8080/api/dashboard/cars/available?page=${page}&size=${size}`
  );
  const data = await response.json();
  return data;
};

// Get cars by price range
const fetchCarsByPrice = async (minPrice, maxPrice, page = 0) => {
  const response = await fetch(
    `http://localhost:8080/api/dashboard/cars/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}&page=${page}&size=20`
  );
  const data = await response.json();
  return data;
};

// Natural language query
const askQuestion = async (question) => {
  const response = await fetch('http://localhost:8080/api/query', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ question })
  });
  const data = await response.json();
  return data;
};
```

---

## Error Responses

All endpoints return appropriate HTTP status codes:

- **200 OK**: Successful request
- **400 Bad Request**: Invalid parameters
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

**Error Response Format:**
```json
{
  "error": "Error message describing what went wrong"
}
```

---

## Pagination

All list endpoints support pagination with the following response structure:

```json
{
  "content": [...],           // Array of items
  "totalPages": 5,            // Total number of pages
  "totalElements": 100,       // Total number of items
  "size": 20,                 // Page size
  "number": 0,                // Current page (0-indexed)
  "first": true,              // Is this the first page?
  "last": false,              // Is this the last page?
  "numberOfElements": 20,     // Number of items in this page
  "empty": false              // Is the page empty?
}
```

---

## Rate Limiting & Performance

- Dashboard endpoints are optimized for fast response times
- Use pagination for large datasets
- Consider caching dashboard stats on the frontend (refresh every 5-10 minutes)
- NL2SQL endpoint may take 2-3 seconds due to LLM processing
