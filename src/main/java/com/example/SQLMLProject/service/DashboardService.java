package com.example.SQLMLProject.service;

import com.example.SQLMLProject.dto.DashboardStats;
import com.example.SQLMLProject.repository.CarRepository;
import com.example.SQLMLProject.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for generating dashboard statistics and summaries.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    
    private final CarRepository carRepository;
    private final SaleRepository saleRepository;
    
    /**
     * Get comprehensive dashboard statistics.
     */
    public DashboardStats getDashboardStats() {
        log.info("Generating dashboard statistics");
        
        DashboardStats stats = new DashboardStats();
        
        // Car statistics
        stats.setTotalCars(carRepository.count());
        stats.setAvailableCars(carRepository.countByStatus("available"));
        stats.setSoldCars(carRepository.countByStatus("sold"));
        stats.setReservedCars(carRepository.countByStatus("reserved"));
        
        // Sales statistics
        stats.setTotalSales(saleRepository.getTotalSalesCount());
        stats.setTotalRevenue(saleRepository.getTotalRevenue() != null ? saleRepository.getTotalRevenue() : BigDecimal.ZERO);
        stats.setAverageSalePrice(saleRepository.getAverageSalePrice() != null ? saleRepository.getAverageSalePrice() : BigDecimal.ZERO);
        
        // Inventory by make
        List<Object[]> inventoryData = carRepository.getInventoryByMake();
        Map<String, Long> inventoryByMake = inventoryData.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue()
                ));
        stats.setInventoryByMake(inventoryByMake);
        
        // Top recent sales
        var topSales = saleRepository.findAllByOrderBySaleDateDesc(PageRequest.of(0, 5));
        List<Map<String, Object>> topSalesList = topSales.getContent().stream()
                .map(sale -> {
                    Map<String, Object> saleMap = new HashMap<>();
                    saleMap.put("id", sale.getId());
                    saleMap.put("carMake", sale.getCar().getMake());
                    saleMap.put("carModel", sale.getCar().getModel());
                    saleMap.put("customerName", sale.getCustomer().getName());
                    saleMap.put("saleDate", sale.getSaleDate());
                    saleMap.put("salePrice", sale.getSalePrice());
                    return saleMap;
                })
                .collect(Collectors.toList());
        stats.setTopSales(topSalesList);
        
        log.info("Dashboard statistics generated successfully");
        return stats;
    }
}
