package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.GameAnalysis;
import com.edwardjtan.riot.model.MatchData;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GameTrackerService {

  private static final Logger log = LoggerFactory.getLogger(GameTrackerService.class);

  private final SummonerService summonerService;
  private final MatchService matchService;
  private final GameAnalysisService gameAnalysisService;
  private final ClaudeService claudeService;

  public GameTrackerService(SummonerService summonerService,
                            MatchService matchService,
                            GameAnalysisService gameAnalysisService,
                            ClaudeService claudeService) {
    this.summonerService = summonerService;
    this.matchService = matchService;
    this.gameAnalysisService = gameAnalysisService;
    this.claudeService = claudeService;
  }

  public String generateGameReport(String gameName, String tagLine) {
    try {
      log.info("Starting game report generation for {}#{}", gameName, tagLine);

      // Get PUUID from Riot ID
      String puuid = summonerService.getPuuidByRiotId(gameName, tagLine);
      log.info("Retrieved PUUID: {}", puuid);

      // Get latest match by PUUID using Riot API v5
      MatchData latestMatch = matchService.getLatestMatchByPuuid(puuid);
      log.info("Retrieved latest match: {}", latestMatch.getMetadata().getMatchId());

      // Analyze match using PUUID
      GameAnalysis analysis = gameAnalysisService.analyzeMatch(latestMatch, puuid);
      log.info("Completed match analysis");

      String report = claudeService.generateGameReport(analysis);
      log.info("Generated game report successfully");

      return report;
    } catch (Exception e) {
      log.error("Error generating game report for {}#{}", gameName, tagLine, e);
      throw new RuntimeException("Failed to generate game report: " + e.getMessage(), e);
    }
  }

  public String generateGameReportByMatchId(String matchId, String puuid) {
    try {
      log.info("Starting game report generation for match ID: {}", matchId);

      // Get match data by match ID
      MatchData matchData = matchService.getMatchById(matchId);
      log.info("Retrieved match data for: {}", matchId);

      // Analyze match using PUUID
      GameAnalysis analysis = gameAnalysisService.analyzeMatch(matchData, puuid);
      log.info("Completed match analysis");

      String report = claudeService.generateGameReport(analysis);
      log.info("Generated game report successfully");

      return report;
    } catch (Exception e) {
      log.error("Error generating game report for match ID: {}", matchId, e);
      throw new RuntimeException("Failed to generate game report for match: " + e.getMessage(), e);
    }
  }
}
