package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.model.GameReportComparison;
import com.edwardjtan.riot.service.GameReportComparisonService;
import com.edwardjtan.riot.service.GameTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-tracker")
@CrossOrigin(origins = "*")
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

  @GetMapping("/report-by-match")
  public ResponseEntity<String> getGameReportByMatch(
    @RequestParam String matchId,
    @RequestParam String puuid
  ) {
    try {
      String report = gameTrackerService.generateGameReportByMatchId(matchId, puuid);
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
    @RequestParam(required = false, defaultValue = "claude-3-5-haiku-20241022") String anthropicModel,
    @RequestParam(required = false, defaultValue = "gpt-4o-mini") String openaiModel
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

  /**
   * Compare different AI providers for generating game reports by match ID
   * GET /api/game-tracker/compare-by-match?matchId=MATCH_ID&puuid=PUUID&anthropicModel=MODEL&openaiModel=MODEL
   * Returns JSON with speed, token usage, and reports for each model
   */
  @GetMapping("/compare-by-match")
  public ResponseEntity<List<GameReportComparison>> compareGameReportsByMatchId(
    @RequestParam String matchId,
    @RequestParam String puuid,
    @RequestParam(required = false, defaultValue = "claude-3-5-haiku-20241022") String anthropicModel,
    @RequestParam(required = false, defaultValue = "gpt-4o-mini") String openaiModel
  ) {
    try {
      List<GameReportComparison> comparisons = comparisonService.compareGameReportsByMatchId(
        matchId, puuid, anthropicModel, openaiModel
      );
      return ResponseEntity.ok(comparisons);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }
}
