package com.example.SQLMLProject.service;

import com.example.SQLMLProject.dto.QueryRequest;
import com.example.SQLMLProject.dto.QueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for converting natural language questions to SQL queries using OpenAI.
 * Handles prompt building, LLM communication, SQL validation, and execution.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QueryService {

    private final ChatClient chatClient;
    private final JdbcTemplate jdbcTemplate;

    // Database schema context for the LLM
    private static final String SCHEMA_CONTEXT = """
            You are an expert SQL developer. Convert the user's natural language question into a valid PostgreSQL SELECT query.
            
            Database Schema:
            - Car(id, make, model, year, price, mileage, status)
            - Customer(id, name, email, phone, city)
            - Sale(id, car_id, customer_id, sale_date, sale_price)
            
            Rules:
            1. Return ONLY the SQL query, nothing else
            2. Use proper SQL syntax for PostgreSQL
            3. Include column names in the SELECT clause
            4. Use appropriate JOINs if needed
            5. Add WHERE clauses for filtering
            6. Do NOT include any markdown formatting, backticks, or explanations
            
            User question: {question}
            
            SQL Query:
            """;

    /**
     * Processes a natural language query and returns SQL with results.
     *
     * @param request QueryRequest containing the natural language question
     * @return QueryResponse with generated SQL and query results
     */
    public QueryResponse processQuery(QueryRequest request) {
        try {
            log.info("Processing query: {}", request.getQuestion());

            // Step 1: Generate SQL from natural language using OpenAI
            String generatedSql = generateSqlFromNaturalLanguage(request.getQuestion());
            log.info("Generated SQL: {}", generatedSql);

            // Step 2: Validate the SQL
            if (!isValidSelectQuery(generatedSql)) {
                log.warn("Invalid SQL query detected: {}", generatedSql);
                return new QueryResponse(generatedSql, null, "Invalid SQL: Only SELECT queries are allowed");
            }

            // Step 3: Execute the SQL
            List<Map<String, Object>> results = executeSql(generatedSql);
            log.info("Query executed successfully, returned {} rows", results.size());

            return new QueryResponse(generatedSql, results, null);

        } catch (Exception e) {
            log.error("Error processing query", e);
            return new QueryResponse(null, null, "Error: " + e.getMessage());
        }
    }

    /**
     * Generates SQL query from natural language using OpenAI.
     *
     * @param question Natural language question
     * @return Generated SQL query
     */
    private String generateSqlFromNaturalLanguage(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(SCHEMA_CONTEXT);
        Prompt prompt = promptTemplate.create(Map.of("question", question));

        String response = chatClient.prompt(prompt)
                .call()
                .content();

        // Clean up the response - remove markdown formatting if present
        return cleanSqlResponse(response);
    }

    /**
     * Cleans up the SQL response from the LLM.
     * Removes markdown formatting, extra whitespace, etc.
     *
     * @param response Raw response from LLM
     * @return Cleaned SQL query
     */
    private String cleanSqlResponse(String response) {
        // Remove markdown code blocks if present
        response = response.replaceAll("```sql\\s*", "");
        response = response.replaceAll("```\\s*", "");
        response = response.trim();

        // Remove leading/trailing whitespace and newlines
        response = response.replaceAll("^\\s+", "");
        response = response.replaceAll("\\s+$", "");

        return response;
    }

    /**
     * Validates that the SQL query is a safe SELECT query.
     * Rejects UPDATE, DELETE, INSERT, DROP, and other dangerous operations.
     *
     * @param sql SQL query to validate
     * @return true if valid SELECT query, false otherwise
     */
    private boolean isValidSelectQuery(String sql) {
        String upperSql = sql.trim().toUpperCase();

        // Must start with SELECT
        if (!upperSql.startsWith("SELECT")) {
            log.warn("Query does not start with SELECT: {}", sql);
            return false;
        }

        // Reject dangerous operations
        String[] dangerousKeywords = {"UPDATE", "DELETE", "INSERT", "DROP", "ALTER", "TRUNCATE", "EXEC", "EXECUTE"};
        for (String keyword : dangerousKeywords) {
            if (upperSql.contains(keyword)) {
                log.warn("Query contains dangerous keyword: {}", keyword);
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the SQL query against the database.
     *
     * @param sql SQL query to execute
     * @return List of result rows as maps
     */
    private List<Map<String, Object>> executeSql(String sql) {
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            log.error("Error executing SQL: {}", sql, e);
            throw new RuntimeException("Database query failed: " + e.getMessage(), e);
        }
    }
}
