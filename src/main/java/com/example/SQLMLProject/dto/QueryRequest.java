package com.example.SQLMLProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for incoming natural language query requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private String question;
}
