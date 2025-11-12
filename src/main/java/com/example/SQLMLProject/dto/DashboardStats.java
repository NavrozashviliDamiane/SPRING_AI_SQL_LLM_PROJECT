package com.example.SQLMLProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO for dashboard statistics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalCars;
    private long availableCars;
    private long soldCars;
    private long reservedCars;
    private BigDecimal totalRevenue;
    private BigDecimal averageSalePrice;
    private long totalSales;
    private Map<String, Long> inventoryByMake;
    private List<Map<String, Object>> topSales;
}
