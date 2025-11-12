package com.example.SQLMLProject.repository;

import com.example.SQLMLProject.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customers by city.
     */
    Page<Customer> findByCity(String city, Pageable pageable);
    
    /**
     * Find customers by name (partial match).
     */
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Get customer count by city.
     */
    @Query("SELECT c.city, COUNT(c) FROM Customer c GROUP BY c.city ORDER BY COUNT(c) DESC")
    List<Object[]> getCustomerCountByCity();
}
