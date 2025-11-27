package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.model.APIComparisonResult;
import com.edwardjtan.riot.service.APIComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comparison")
@CrossOrigin(origins = "*")
public class APIComparisonController {

    @Autowired
    private APIComparisonService comparisonService;

    /**
     * Compare multiple API providers
     * GET /api/comparison/compare?prompt=YOUR_PROMPT&anthropicModel=MODEL&openaiModel=MODEL
     */
    @GetMapping("/compare")
    public ResponseEntity<List<APIComparisonResult>> compareProviders(
            @RequestParam String prompt,
            @RequestParam(required = false, defaultValue = "claude-sonnet-4-20250514") String anthropicModel,
            @RequestParam(required = false, defaultValue = "gpt-4o") String openaiModel) {

        try {
            List<APIComparisonResult> results = comparisonService.compareProviders(prompt, anthropicModel, openaiModel);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Test Anthropic API only
     * GET /api/comparison/anthropic?prompt=YOUR_PROMPT&model=MODEL
     */
    @GetMapping("/anthropic")
    public ResponseEntity<APIComparisonResult> testAnthropic(
            @RequestParam String prompt,
            @RequestParam(required = false, defaultValue = "claude-sonnet-4-20250514") String model) {

        try {
            APIComparisonResult result = comparisonService.testAnthropic(prompt, model);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Test OpenAI API only
     * GET /api/comparison/openai?prompt=YOUR_PROMPT&model=MODEL
     */
    @GetMapping("/openai")
    public ResponseEntity<APIComparisonResult> testOpenAI(
            @RequestParam String prompt,
            @RequestParam(required = false, defaultValue = "gpt-4o") String model) {

        try {
            APIComparisonResult result = comparisonService.testOpenAI(prompt, model);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
