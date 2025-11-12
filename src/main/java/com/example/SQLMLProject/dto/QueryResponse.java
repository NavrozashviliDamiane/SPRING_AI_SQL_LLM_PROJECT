package com.example.SQLMLProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * DTO for query response containing generated SQL and results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private String sql;
    private List<Map<String, Object>> result;
    private String error;
}
