package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.model.MatchDetails;
import com.edwardjtan.riot.model.MatchSummary;
import com.edwardjtan.riot.model.RadarChartStats;
import com.edwardjtan.riot.service.MatchService;
import com.edwardjtan.riot.service.SummonerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MatchController {

  private final MatchService matchService;
  private final SummonerService summonerService;

  public MatchController(MatchService matchService, SummonerService summonerService) {
    this.matchService = matchService;
    this.summonerService = summonerService;
  }

  /**
   * Get recent matches for a player
   * GET /api/matches?gameName=NAME&tagLine=TAG&count=20
   */
  @GetMapping("/matches")
  public ResponseEntity<List<MatchSummary>> getRecentMatches(
    @RequestParam String gameName,
    @RequestParam String tagLine,
    @RequestParam(required = false, defaultValue = "20") int count
  ) {
    try {
      // Get PUUID from Riot ID
      String puuid = summonerService.getPuuidByRiotId(gameName, tagLine);

      // Get recent matches
      List<MatchSummary> matches = matchService.getRecentMatches(puuid, count);

      return ResponseEntity.ok(matches);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * Get detailed match information
   * GET /api/match/{matchId}?puuid=PUUID
   */
  @GetMapping("/match/{matchId}")
  public ResponseEntity<MatchDetails> getMatchDetails(
    @PathVariable String matchId,
    @RequestParam String puuid
  ) {
    try {
      MatchDetails details = matchService.getMatchDetails(matchId, puuid);
      return ResponseEntity.ok(details);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * Get radar chart comparison stats for a match
   * GET /api/match/{matchId}/radar?puuid=PUUID
   */
  @GetMapping("/match/{matchId}/radar")
  public ResponseEntity<RadarChartStats> getRadarChartStats(
    @PathVariable String matchId,
    @RequestParam String puuid
  ) {
    try {
      RadarChartStats radarStats = matchService.getRadarChartStats(matchId, puuid);
      return ResponseEntity.ok(radarStats);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }
}
