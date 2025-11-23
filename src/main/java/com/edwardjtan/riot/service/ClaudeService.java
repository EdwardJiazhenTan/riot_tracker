package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.GameAnalysis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaudeService {

  private static final Logger log = LoggerFactory.getLogger(ClaudeService.class);
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${anthropic.api.key}")
  private String apiKey;

  public String generateGameReport(GameAnalysis analysis) {
    try {
      log.info("Generating game report for match {}", analysis.getMatchId());

      String prompt = buildPrompt(analysis);

      // Build request body
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("model", "claude-sonnet-4-20250514");
      requestBody.put("max_tokens", 2048);
      requestBody.put("messages", List.of(
        Map.of("role", "user", "content", prompt)
      ));

      // Set headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.set("x-api-key", apiKey);
      headers.set("anthropic-version", "2023-06-01");

      HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

      // Make API call
      String response = restTemplate.postForObject(
        "https://api.anthropic.com/v1/messages",
        request,
        String.class
      );

      // Parse response
      JsonNode responseJson = objectMapper.readTree(response);
      JsonNode content = responseJson.get("content");

      if (content != null && content.isArray() && content.size() > 0) {
        String text = content.get(0).get("text").asText();
        log.info("Successfully generated game report");
        return text;
      }

      log.warn("No content in Claude response");
      return "Unable to generate report";
    } catch (Exception e) {
      log.error("Error generating game report", e);
      throw new RuntimeException("Failed to generate game report", e);
    }
  }

  private String buildPrompt(GameAnalysis analysis) {
    StringBuilder prompt = new StringBuilder();
    prompt.append("You are a professional League of Legends analyst. Generate a detailed game report based on the following match data:\n\n");
    prompt.append("Match ID: ").append(analysis.getMatchId()).append("\n");
    prompt.append("Player: ").append(analysis.getPlayerName()).append("\n");
    prompt.append("Champion: ").append(analysis.getChampion()).append("\n");
    prompt.append("Role: ").append(analysis.getRole()).append("\n");
    prompt.append("Result: ").append(analysis.isVictory() ? "VICTORY" : "DEFEAT").append("\n");
    prompt.append("KDA: ").append(analysis.getKills()).append("/")
      .append(analysis.getDeaths()).append("/")
      .append(analysis.getAssists()).append("\n");
    prompt.append("Game Duration: ").append(analysis.getGameDuration() / 60)
      .append(" minutes ").append(analysis.getGameDuration() % 60).append(" seconds\n\n");

    prompt.append("Statistics:\n");
    analysis.getStats().forEach((key, value) ->
      prompt.append("- ").append(key).append(": ").append(value).append("\n")
    );

    prompt.append("\nKey Events (").append(analysis.getImportantEvents().size()).append(" total):\n");
    analysis.getImportantEvents().forEach(event ->
      prompt.append("- ").append(event.toString()).append("\n")
    );

    prompt.append("\nPlease provide:\n");
    prompt.append("1. Overall performance summary\n");
    prompt.append("2. Key strengths and highlights from the game\n");
    prompt.append("3. Areas for improvement\n");
    prompt.append("4. Notable moments or plays\n");
    prompt.append("5. Recommendations for future games\n\n");
    prompt.append("Keep the report concise, professional, and actionable.");

    return prompt.toString();
  }
}
