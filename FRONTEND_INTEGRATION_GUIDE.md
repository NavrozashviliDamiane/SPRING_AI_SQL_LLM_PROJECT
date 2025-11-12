# Frontend Integration Guide - Car Dealer NL2SQL API

## Table of Contents
1. [Base Configuration](#base-configuration)
2. [Health Check](#health-check)
3. [Natural Language to SQL](#natural-language-to-sql)
4. [Dashboard Statistics](#dashboard-statistics)
5. [Cars Endpoints](#cars-endpoints)
6. [Sales Endpoints](#sales-endpoints)
7. [Customers Endpoints](#customers-endpoints)
8. [Error Handling](#error-handling)
9. [Pagination Guide](#pagination-guide)
10. [Frontend Implementation Examples](#frontend-implementation-examples)

---

## Base Configuration

### API Base URL
```
http://localhost:8080/api
```

### Common Headers
```
Content-Type: application/json
Accept: application/json
```

### Environment Variables (for frontend)
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

---

## Health Check

### Endpoint
```
GET /health
```

### Request
```bash
curl -X GET http://localhost:8080/api/health
```

### Response (200 OK)
```
Service is running
```

### Use Case
- Check if backend is available before making other requests
- Implement retry logic if this fails

### Frontend Implementation
```javascript
async function checkServiceHealth() {
  try {
    const response = await fetch(`${API_BASE_URL}/health`);
    return response.ok;
  } catch (error) {
    console.error('Service unavailable:', error);
    return false;
  }
}
```

---

## Natural Language to SQL

### Endpoint
```
POST /query
```

### Request Structure
```json
{
  "question": "Show me all available Toyota cars under $25,000"
}
```

### Request Examples

#### Example 1: Filter by Make and Price
```json
{
  "question": "Show me all available Toyota cars under $25,000"
}
```

#### Example 2: Filter by Status
```json
{
  "question": "How many cars are currently available?"
}
```

#### Example 3: Join Query
```json
{
  "question": "Show me all sales from customers in New York"
}
```

#### Example 4: Aggregation
```json
{
  "question": "What is the total revenue from all sales?"
}
```

### Response Structure (Success)
```json
{
  "sql": "SELECT * FROM car WHERE make = 'Toyota' AND price < 25000 AND status = 'available'",
  "result": [
    {
      "id": 1,
      "make": "Toyota",
      "model": "Corolla",
      "year": 2023,
      "price": 22500.00,
      "mileage": 5000,
      "status": "available"
    },
    {
      "id": 2,
      "make": "Toyota",
      "model": "Camry",
      "year": 2022,
      "price": 24000.00,
      "mileage": 15000,
      "status": "available"
    }
  ],
  "error": null
}
```

### Response Structure (Error - Invalid Query)
```json
{
  "sql": "DELETE FROM car WHERE id = 1",
  "result": null,
  "error": "Invalid SQL: Only SELECT queries are allowed"
}
```

### Response Structure (Error - Empty Question)
```json
{
  "sql": null,
  "result": null,
  "error": "Question cannot be empty"
}
```

### HTTP Status Codes
- `200 OK`: Query executed successfully
- `400 Bad Request`: Invalid question or empty input
- `500 Internal Server Error`: Server error or LLM error

### Frontend Implementation
```javascript
async function askNaturalLanguageQuestion(question) {
  try {
    const response = await fetch(`${API_BASE_URL}/query`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ question })
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    
    if (data.error) {
      console.error('Query Error:', data.error);
      return { success: false, error: data.error };
    }

    return {
      success: true,
      sql: data.sql,
      results: data.result
    };
  } catch (error) {
    console.error('Request failed:', error);
    return { success: false, error: error.message };
  }
}
```

---

## Dashboard Statistics

### Endpoint
```
GET /dashboard/stats
```

### Request
```bash
curl -X GET http://localhost:8080/api/dashboard/stats
```

### Response Structure (200 OK)
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
    },
    {
      "id": 2,
      "carMake": "Honda",
      "carModel": "Accord",
      "customerName": "Jane Smith",
      "saleDate": "2024-01-14",
      "salePrice": 22000.00
    }
  ]
}
```

### Field Descriptions
| Field | Type | Description |
|-------|------|-------------|
| totalCars | number | Total number of cars in inventory |
| availableCars | number | Cars with status 'available' |
| soldCars | number | Cars with status 'sold' |
| reservedCars | number | Cars with status 'reserved' |
| totalRevenue | decimal | Sum of all sale prices |
| averageSalePrice | decimal | Average price per sale |
| totalSales | number | Total number of sales |
| inventoryByMake | object | Count of cars grouped by make |
| topSales | array | 5 most recent sales |

### Frontend Implementation
```javascript
async function fetchDashboardStats() {
  try {
    const response = await fetch(`${API_BASE_URL}/dashboard/stats`);
    const data = await response.json();
    
    return {
      stats: {
        totalCars: data.totalCars,
        availableCars: data.availableCars,
        soldCars: data.soldCars,
        reservedCars: data.reservedCars,
        revenue: data.totalRevenue,
        avgPrice: data.averageSalePrice,
        totalSales: data.totalSales
      },
      inventory: data.inventoryByMake,
      recentSales: data.topSales
    };
  } catch (error) {
    console.error('Failed to fetch dashboard stats:', error);
    return null;
  }
}
```

---

## Cars Endpoints

### 1. Get All Cars (with Filtering)

#### Endpoint
```
GET /dashboard/cars
```

#### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| status | string | null | Filter by status (available, sold, reserved) |
| make | string | null | Filter by car make |
| minPrice | decimal | null | Minimum price filter |
| maxPrice | decimal | null | Maximum price filter |
| page | integer | 0 | Page number (0-indexed) |
| size | integer | 20 | Number of items per page |
| sortBy | string | id | Field to sort by (id, price, year, make, model) |
| direction | string | ASC | Sort direction (ASC or DESC) |

#### Request Examples

**Get all cars (first page)**
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars?page=0&size=10"
```

**Get available cars sorted by price**
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars?status=available&sortBy=price&direction=ASC&page=0&size=10"
```

**Get cars by make**
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars?make=Toyota&page=0&size=10"
```

**Get cars in price range**
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars?minPrice=20000&maxPrice=30000&page=0&size=10"
```

#### Response Structure (200 OK)
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
    },
    {
      "id": 2,
      "make": "Toyota",
      "model": "Camry",
      "year": 2022,
      "price": 24000.00,
      "mileage": 15000,
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

#### Frontend Implementation
```javascript
async function fetchCars(filters = {}) {
  const params = new URLSearchParams({
    page: filters.page || 0,
    size: filters.size || 10,
    sortBy: filters.sortBy || 'price',
    direction: filters.direction || 'ASC'
  });

  if (filters.status) params.append('status', filters.status);
  if (filters.make) params.append('make', filters.make);
  if (filters.minPrice) params.append('minPrice', filters.minPrice);
  if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);

  try {
    const response = await fetch(`${API_BASE_URL}/dashboard/cars?${params}`);
    const data = await response.json();
    
    return {
      cars: data.content,
      pagination: {
        currentPage: data.number,
        pageSize: data.size,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        isFirst: data.first,
        isLast: data.last
      }
    };
  } catch (error) {
    console.error('Failed to fetch cars:', error);
    return null;
  }
}
```

---

### 2. Get Available Cars

#### Endpoint
```
GET /dashboard/cars/available
```

#### Query Parameters
| Parameter | Type | Default |
|-----------|------|---------|
| page | integer | 0 |
| size | integer | 20 |

#### Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars/available?page=0&size=10"
```

#### Response Structure
Same as "Get All Cars" response

#### Frontend Implementation
```javascript
async function fetchAvailableCars(page = 0, size = 10) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/dashboard/cars/available?page=${page}&size=${size}`
    );
    const data = await response.json();
    return data.content;
  } catch (error) {
    console.error('Failed to fetch available cars:', error);
    return [];
  }
}
```

---

### 3. Get Cars by Make

#### Endpoint
```
GET /dashboard/cars/make/{make}
```

#### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| make | string | Car manufacturer (e.g., Toyota, Honda, Ford) |

#### Query Parameters
| Parameter | Type | Default |
|-----------|------|---------|
| page | integer | 0 |
| size | integer | 20 |

#### Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars/make/Toyota?page=0&size=10"
```

#### Response Structure
Same as "Get All Cars" response

#### Frontend Implementation
```javascript
async function fetchCarsByMake(make, page = 0, size = 10) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/dashboard/cars/make/${encodeURIComponent(make)}?page=${page}&size=${size}`
    );
    const data = await response.json();
    return data.content;
  } catch (error) {
    console.error(`Failed to fetch ${make} cars:`, error);
    return [];
  }
}
```

---

### 4. Get Cars by Price Range

#### Endpoint
```
GET /dashboard/cars/price-range
```

#### Query Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| minPrice | decimal | Yes | Minimum price |
| maxPrice | decimal | Yes | Maximum price |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20) |

#### Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/cars/price-range?minPrice=20000&maxPrice=30000&page=0&size=10"
```

#### Response Structure
Same as "Get All Cars" response

#### Frontend Implementation
```javascript
async function fetchCarsByPriceRange(minPrice, maxPrice, page = 0, size = 10) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/dashboard/cars/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}&page=${page}&size=${size}`
    );
    const data = await response.json();
    return data.content;
  } catch (error) {
    console.error('Failed to fetch cars by price range:', error);
    return [];
  }
}
```

---

## Sales Endpoints

### Endpoint
```
GET /dashboard/sales
```

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | integer | 0 | Page number (0-indexed) |
| size | integer | 20 | Number of items per page |

### Request
```bash
curl -X GET "http://localhost:8080/api/dashboard/sales?page=0&size=10"
```

### Response Structure (200 OK)
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
    },
    {
      "id": 2,
      "car": {
        "id": 5,
        "make": "Honda",
        "model": "Accord",
        "year": 2022,
        "price": 22000.00,
        "mileage": 25000,
        "status": "sold"
      },
      "customer": {
        "id": 2,
        "name": "Jane Smith",
        "email": "jane.smith@email.com",
        "phone": "555-0102",
        "city": "Los Angeles"
      },
      "saleDate": "2024-01-14",
      "salePrice": 21500.00
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

### Frontend Implementation
```javascript
async function fetchSales(page = 0, size = 10) {
  try {
    const response = await fetch(
      `${API_BASE_URL}/dashboard/sales?page=${page}&size=${size}`
    );
    const data = await response.json();
    
    return {
      sales: data.content,
      pagination: {
        currentPage: data.number,
        pageSize: data.size,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        isFirst: data.first,
        isLast: data.last
      }
    };
  } catch (error) {
    console.error('Failed to fetch sales:', error);
    return null;
  }
}
```

---

## Customers Endpoints

### 1. Get All Customers

#### Endpoint
```
GET /dashboard/customers
```

#### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| city | string | null | Filter by city |
| name | string | null | Filter by name (partial match, case-insensitive) |
| page | integer | 0 | Page number (0-indexed) |
| size | integer | 20 | Number of items per page |

#### Request Examples

**Get all customers**
```bash
curl -X GET "http://localhost:8080/api/dashboard/customers?page=0&size=10"
```

**Get customers by city**
```bash
curl -X GET "http://localhost:8080/api/dashboard/customers?city=New%20York&page=0&size=10"
```

**Get customers by name**
```bash
curl -X GET "http://localhost:8080/api/dashboard/customers?name=John&page=0&size=10"
```

#### Response Structure (200 OK)
```json
{
  "content": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@email.com",
      "phone": "555-0101",
      "city": "New York"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane.smith@email.com",
      "phone": "555-0102",
      "city": "New York"
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
  "totalPages": 1,
  "totalElements": 2,
  "last": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

#### Frontend Implementation
```javascript
async function fetchCustomers(filters = {}) {
  const params = new URLSearchParams({
    page: filters.page || 0,
    size: filters.size || 10
  });

  if (filters.city) params.append('city', filters.city);
  if (filters.name) params.append('name', filters.name);

  try {
    const response = await fetch(`${API_BASE_URL}/dashboard/customers?${params}`);
    const data = await response.json();
    
    return {
      customers: data.content,
      pagination: {
        currentPage: data.number,
        pageSize: data.size,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        isFirst: data.first,
        isLast: data.last
      }
    };
  } catch (error) {
    console.error('Failed to fetch customers:', error);
    return null;
  }
}
```

---

## Error Handling

### Common Error Responses

#### 400 Bad Request
```json
{
  "error": "Question cannot be empty"
}
```

#### 400 Bad Request (Invalid SQL)
```json
{
  "sql": "UPDATE car SET price = 100 WHERE id = 1",
  "result": null,
  "error": "Invalid SQL: Only SELECT queries are allowed"
}
```

#### 500 Internal Server Error
```json
{
  "error": "Internal server error occurred"
}
```

### Frontend Error Handling Pattern
```javascript
async function handleApiCall(url, options = {}) {
  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || `HTTP ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    
    // Show user-friendly error message
    if (error.message.includes('Failed to fetch')) {
      return { error: 'Service unavailable. Please try again later.' };
    }
    
    return { error: error.message };
  }
}
```

---

## Pagination Guide

### Understanding Pagination

All list endpoints return paginated results with the following structure:

```json
{
  "content": [...],           // Array of items
  "pageable": {...},          // Pagination metadata
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

### Pagination Parameters

| Parameter | Type | Default | Max | Description |
|-----------|------|---------|-----|-------------|
| page | integer | 0 | N/A | Page number (0-indexed) |
| size | integer | 20 | 100 | Items per page |

### Frontend Pagination Implementation
```javascript
class PaginationManager {
  constructor(apiEndpoint) {
    this.endpoint = apiEndpoint;
    this.currentPage = 0;
    this.pageSize = 10;
    this.totalPages = 0;
    this.data = [];
  }

  async fetchPage(pageNumber, pageSize = 10) {
    try {
      const response = await fetch(
        `${this.endpoint}?page=${pageNumber}&size=${pageSize}`
      );
      const data = await response.json();
      
      this.currentPage = data.number;
      this.pageSize = data.size;
      this.totalPages = data.totalPages;
      this.data = data.content;
      
      return data;
    } catch (error) {
      console.error('Pagination error:', error);
      return null;
    }
  }

  async nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      return this.fetchPage(this.currentPage + 1, this.pageSize);
    }
    return null;
  }

  async previousPage() {
    if (this.currentPage > 0) {
      return this.fetchPage(this.currentPage - 1, this.pageSize);
    }
    return null;
  }

  async goToPage(pageNumber) {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      return this.fetchPage(pageNumber, this.pageSize);
    }
    return null;
  }
}
```

---

## Frontend Implementation Examples

### React Hook for Dashboard
```javascript
import { useState, useEffect } from 'react';

function useDashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/dashboard/stats`);
        if (!response.ok) throw new Error('Failed to fetch stats');
        const data = await response.json();
        setStats(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  return { stats, loading, error };
}
```

### React Hook for Cars List
```javascript
function useCarsList(filters = {}) {
  const [cars, setCars] = useState([]);
  const [pagination, setPagination] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchCars = async (page = 0) => {
    setLoading(true);
    try {
      const params = new URLSearchParams({
        page,
        size: filters.size || 10,
        ...filters
      });
      
      const response = await fetch(
        `${API_BASE_URL}/dashboard/cars?${params}`
      );
      if (!response.ok) throw new Error('Failed to fetch cars');
      
      const data = await response.json();
      setCars(data.content);
      setPagination({
        currentPage: data.number,
        totalPages: data.totalPages,
        totalElements: data.totalElements
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return { cars, pagination, loading, error, fetchCars };
}
```

### React Component Example
```javascript
function CarsListComponent() {
  const { cars, pagination, loading, error, fetchCars } = useCarsList();

  useEffect(() => {
    fetchCars(0);
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>Make</th>
            <th>Model</th>
            <th>Year</th>
            <th>Price</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {cars.map(car => (
            <tr key={car.id}>
              <td>{car.make}</td>
              <td>{car.model}</td>
              <td>{car.year}</td>
              <td>${car.price.toFixed(2)}</td>
              <td>{car.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
      <div>
        <button 
          onClick={() => fetchCars(pagination.currentPage - 1)}
          disabled={pagination.currentPage === 0}
        >
          Previous
        </button>
        <span>Page {pagination.currentPage + 1} of {pagination.totalPages}</span>
        <button 
          onClick={() => fetchCars(pagination.currentPage + 1)}
          disabled={pagination.currentPage === pagination.totalPages - 1}
        >
          Next
        </button>
      </div>
    </div>
  );
}
```

---

## Best Practices for Frontend Integration

1. **Error Handling**: Always handle errors gracefully and show user-friendly messages
2. **Loading States**: Display loading indicators while fetching data
3. **Caching**: Consider caching dashboard stats (refresh every 5-10 minutes)
4. **Pagination**: Use pagination for large datasets instead of loading all at once
5. **Debouncing**: Debounce search/filter inputs to reduce API calls
6. **Environment Variables**: Use environment variables for API base URL
7. **Request Timeout**: Set reasonable timeouts for API requests (5-10 seconds)
8. **Retry Logic**: Implement retry logic for failed requests
9. **Response Validation**: Validate API responses before using them
10. **Logging**: Log API errors for debugging purposes

---

## Testing Checklist for Frontend

- [ ] Health check endpoint works
- [ ] Dashboard stats load correctly
- [ ] Cars list displays with pagination
- [ ] Filtering by status works
- [ ] Filtering by make works
- [ ] Price range filtering works
- [ ] Sales list displays correctly
- [ ] Customers list displays correctly
- [ ] Natural language query works
- [ ] Error messages display properly
- [ ] Pagination navigation works
- [ ] Loading states display correctly
