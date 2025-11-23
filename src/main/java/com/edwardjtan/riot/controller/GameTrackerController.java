package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.service.GameTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-tracker")
public class GameTrackerController {

  private final GameTrackerService gameTrackerService;

  public GameTrackerController(GameTrackerService gameTrackerService) {
    this.gameTrackerService = gameTrackerService;
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
}
