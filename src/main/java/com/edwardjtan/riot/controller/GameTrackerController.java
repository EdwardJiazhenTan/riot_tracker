package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.model.GameReportComparison;
import com.edwardjtan.riot.service.GameReportComparisonService;
import com.edwardjtan.riot.service.GameTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-tracker")
public class GameTrackerController {

  private final GameTrackerService gameTrackerService;
  private final GameReportComparisonService comparisonService;

  public GameTrackerController(GameTrackerService gameTrackerService,
                               GameReportComparisonService comparisonService) {
    this.gameTrackerService = gameTrackerService;
    this.comparisonService = comparisonService;
  }

  @GetMapping("/report")
  public ResponseEntity<String> getGameReport(
    @RequestParam String gameName,
    @RequestParam String tagLine
  ) {
    try {
      String report = gameTrackerService.generateGameReport(gameName, tagLine);
      return ResponseEntity.ok(report);
    } catch (Exception e) {
      String errorDetails = "Error: " + e.getMessage();
      if (e.getCause() != null) {
        errorDetails += "\nCause: " + e.getCause().getMessage();
      }
      e.printStackTrace();
      return ResponseEntity.status(500).body(errorDetails);
    }
  }

  @GetMapping("/report/{gameName}/{tagLine}")
  public ResponseEntity<String> getGameReportPath(
    @PathVariable String gameName,
    @PathVariable String tagLine
  ) {
    try {
      String report = gameTrackerService.generateGameReport(gameName, tagLine);
      return ResponseEntity.ok(report);
    } catch (Exception e) {
      String errorDetails = "Error: " + e.getMessage();
      if (e.getCause() != null) {
        errorDetails += "\nCause: " + e.getCause().getMessage();
      }
      e.printStackTrace();
      return ResponseEntity.status(500).body(errorDetails);
    }
  }

  /**
   * Compare different AI providers for generating game reports
   * GET /api/game-tracker/compare?gameName=NAME&tagLine=TAG&anthropicModel=MODEL&openaiModel=MODEL
   */
  @GetMapping("/compare")
  public ResponseEntity<List<GameReportComparison>> compareGameReports(
    @RequestParam String gameName,
    @RequestParam String tagLine,
    @RequestParam(required = false, defaultValue = "claude-sonnet-4-20250514") String anthropicModel,
    @RequestParam(required = false, defaultValue = "gpt-4o") String openaiModel
  ) {
    try {
      List<GameReportComparison> comparisons = comparisonService.compareGameReports(
        gameName, tagLine, anthropicModel, openaiModel
      );
      return ResponseEntity.ok(comparisons);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }
}
