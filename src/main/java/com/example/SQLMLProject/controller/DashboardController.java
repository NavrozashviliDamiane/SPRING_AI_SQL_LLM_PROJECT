package com.example.SQLMLProject.controller;

import com.example.SQLMLProject.dto.DashboardStats;
import com.example.SQLMLProject.entity.Car;
import com.example.SQLMLProject.entity.Customer;
import com.example.SQLMLProject.entity.Sale;
import com.example.SQLMLProject.repository.CarRepository;
import com.example.SQLMLProject.repository.CustomerRepository;
import com.example.SQLMLProject.repository.SaleRepository;
import com.example.SQLMLProject.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST Controller for dashboard endpoints.
 * Provides aggregated data and list endpoints for frontend dashboard.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    
    private final DashboardService dashboardService;
    private final CarRepository carRepository;
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Get comprehensive dashboard statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        log.info("Fetching dashboard statistics");
        DashboardStats stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Get all cars with optional filtering and pagination.
     */
    @GetMapping("/cars")
    public ResponseEntity<Page<Car>> getCars(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        
        log.info("Fetching cars - status: {}, make: {}, page: {}, size: {}", status, make, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Car> cars;
        
        if (status != null && !status.isEmpty()) {
            cars = carRepository.findByStatus(status, pageable);
        } else if (make != null && !make.isEmpty()) {
            cars = carRepository.findByMake(make, pageable);
        } else if (minPrice != null && maxPrice != null) {
            cars = carRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else {
            cars = carRepository.findAll(pageable);
        }
        
        return ResponseEntity.ok(cars);
    }
    
    /**
     * Get all sales with pagination.
     */
    @GetMapping("/sales")
    public ResponseEntity<Page<Sale>> getSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching sales - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "saleDate"));
        Page<Sale> sales = saleRepository.findAllByOrderBySaleDateDesc(pageable);
        
        return ResponseEntity.ok(sales);
    }
    
    /**
     * Get all customers with optional filtering and pagination.
     */
    @GetMapping("/customers")
    public ResponseEntity<Page<Customer>> getCustomers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching customers - city: {}, name: {}, page: {}, size: {}", city, name, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Customer> customers;
        
        if (city != null && !city.isEmpty()) {
            customers = customerRepository.findByCity(city, pageable);
        } else if (name != null && !name.isEmpty()) {
            customers = customerRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }
        
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Get available cars only.
     */
    @GetMapping("/cars/available")
    public ResponseEntity<Page<Car>> getAvailableCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching available cars - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
        Page<Car> cars = carRepository.findByStatus("available", pageable);
        
        return ResponseEntity.ok(cars);
    }
    
    /**
     * Get cars by specific make.
     */
    @GetMapping("/cars/make/{make}")
    public ResponseEntity<Page<Car>> getCarsByMake(
            @PathVariable String make,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching cars by make: {} - page: {}, size: {}", make, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
        Page<Car> cars = carRepository.findByMake(make, pageable);
        
        return ResponseEntity.ok(cars);
    }
    
    /**
     * Get cars within a price range.
     */
    @GetMapping("/cars/price-range")
    public ResponseEntity<Page<Car>> getCarsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Fetching cars by price range: {} - {} - page: {}, size: {}", minPrice, maxPrice, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
        Page<Car> cars = carRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        
        return ResponseEntity.ok(cars);
    }
}
