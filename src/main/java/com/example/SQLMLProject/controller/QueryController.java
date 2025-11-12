package com.example.SQLMLProject.controller;

import com.example.SQLMLProject.dto.QueryRequest;
import com.example.SQLMLProject.dto.QueryResponse;
import com.example.SQLMLProject.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling natural language to SQL conversion requests.
 * Exposes the /api/query endpoint for processing user questions.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QueryController {

    private final QueryService queryService;

    /**
     * Converts a natural language question into SQL and executes it.
     *
     * @param request QueryRequest containing the natural language question
     * @return QueryResponse with generated SQL and results
     */
    @PostMapping("/query")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {
        log.info("Received query request: {}", request.getQuestion());

        if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new QueryResponse(null, null, "Question cannot be empty"));
        }

        QueryResponse response = queryService.processQuery(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint.
     *
     * @return Simple health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is running");
    }
}
