package com.example.SQLMLProject.repository;

import com.example.SQLMLProject.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Sale entity with custom queries for dashboard.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    /**
     * Find recent sales ordered by date descending.
     */
    Page<Sale> findAllByOrderBySaleDateDesc(Pageable pageable);
    
    /**
     * Find sales within a date range.
     */
    Page<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Get total sales revenue.
     */
    @Query("SELECT SUM(s.salePrice) FROM Sale s")
    BigDecimal getTotalRevenue();
    
    /**
     * Get total sales count.
     */
    @Query("SELECT COUNT(s) FROM Sale s")
    long getTotalSalesCount();
    
    /**
     * Get average sale price.
     */
    @Query("SELECT AVG(s.salePrice) FROM Sale s")
    BigDecimal getAverageSalePrice();
    
    /**
     * Get sales by make.
     */
    @Query("SELECT c.make, COUNT(s), SUM(s.salePrice) FROM Sale s JOIN s.car c GROUP BY c.make ORDER BY COUNT(s) DESC")
    List<Object[]> getSalesByMake();
    
    /**
     * Get sales revenue by month for current year.
     */
    @Query("SELECT FUNCTION('DATE_TRUNC', 'month', s.saleDate), COUNT(s), SUM(s.salePrice) " +
           "FROM Sale s WHERE YEAR(s.saleDate) = YEAR(CURRENT_DATE) " +
           "GROUP BY FUNCTION('DATE_TRUNC', 'month', s.saleDate) " +
           "ORDER BY FUNCTION('DATE_TRUNC', 'month', s.saleDate) DESC")
    List<Object[]> getSalesRevenueByMonth();
}
