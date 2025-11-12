package com.example.SQLMLProject.repository;

import com.example.SQLMLProject.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Car entity with custom queries for dashboard.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    /**
     * Find all cars with a specific status.
     */
    Page<Car> findByStatus(String status, Pageable pageable);
    
    /**
     * Find cars by make.
     */
    Page<Car> findByMake(String make, Pageable pageable);
    
    /**
     * Find cars within a price range.
     */
    Page<Car> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    /**
     * Find cars by year.
     */
    Page<Car> findByYear(Integer year, Pageable pageable);
    
    /**
     * Count cars by status.
     */
    long countByStatus(String status);
    
    /**
     * Get inventory count by make.
     */
    @Query("SELECT c.make, COUNT(c) FROM Car c GROUP BY c.make ORDER BY COUNT(c) DESC")
    List<Object[]> getInventoryByMake();
    
    /**
     * Get average price by make.
     */
    @Query("SELECT c.make, AVG(c.price) FROM Car c GROUP BY c.make ORDER BY AVG(c.price) DESC")
    List<Object[]> getAveragePriceByMake();
}
