package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.MatchData;
import com.edwardjtan.riot.model.MatchSummary;
import com.edwardjtan.riot.model.MatchDetails;
import com.edwardjtan.riot.model.RadarChartStats;
import com.edwardjtan.riot.model.PerformanceBenchmarks;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Timeline;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.merakianalytics.orianna.types.common.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class MatchService {

  private static final Logger log = LoggerFactory.getLogger(MatchService.class);
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${riot.api.key}")
  private String apiKey;

  public MatchData getLatestMatchByPuuid(String puuid) {
    try {
      log.info("Fetching latest match for PUUID: {}", puuid);

      // Get match IDs using Riot API v5
      String matchListUrl = String.format(
        "https://americas.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=0&count=1&api_key=%s",
        puuid, apiKey
      );

      String[] matchIds = restTemplate.getForObject(matchListUrl, String[].class);

      if (matchIds == null || matchIds.length == 0) {
        throw new RuntimeException("No matches found for PUUID: " + puuid);
      }

      String matchId = matchIds[0];
      log.info("Found latest match ID: {}", matchId);

      return getMatchById(matchId);
    } catch (Exception e) {
      log.error("Error fetching latest match for PUUID: {}", puuid, e);
      throw new RuntimeException("Failed to fetch latest match for PUUID: " + puuid, e);
    }
  }

  public MatchData getMatchById(String matchId) {
    try {
      log.info("Fetching match data for match ID: {}", matchId);

      // Get full match data using Riot API v5 directly
      String matchUrl = String.format(
        "https://americas.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s",
        matchId, apiKey
      );

      MatchData matchData = restTemplate.getForObject(matchUrl, MatchData.class);

      if (matchData == null) {
        throw new RuntimeException("Failed to fetch match data for: " + matchId);
      }

      log.info("Successfully fetched match data");
      return matchData;
    } catch (Exception e) {
      log.error("Error fetching match by ID: {}", matchId, e);
      throw new RuntimeException("Failed to fetch match by ID: " + matchId, e);
    }
  }

  public List<MatchSummary> getRecentMatches(String puuid, int count) {
    try {
      log.info("Fetching {} recent matches for PUUID: {}", count, puuid);

      // Get match IDs using Riot API v5
      String matchListUrl = String.format(
        "https://americas.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=0&count=%d&api_key=%s",
        puuid, count, apiKey
      );

      String[] matchIds = restTemplate.getForObject(matchListUrl, String[].class);

      if (matchIds == null || matchIds.length == 0) {
        log.warn("No matches found for PUUID: {}", puuid);
        return new ArrayList<>();
      }

      log.info("Found {} match IDs, fetching details...", matchIds.length);

      // Fetch each match and extract player stats
      List<MatchSummary> summaries = new ArrayList<>();
      for (String matchId : matchIds) {
        try {
          MatchData matchData = getMatchById(matchId);

          // Find the player's participant data
          MatchData.ParticipantDto playerData = matchData.getInfo().getParticipants()
            .stream()
            .filter(p -> p.getPuuid().equals(puuid))
            .findFirst()
            .orElse(null);

          if (playerData != null) {
            MatchSummary summary = new MatchSummary(
              matchData.getMetadata().getMatchId(),
              playerData.getChampionName(),
              playerData.getKills(),
              playerData.getDeaths(),
              playerData.getAssists(),
              playerData.isWin(),
              matchData.getInfo().getGameDuration(),
              matchData.getInfo().getGameCreation()
            );
            summaries.add(summary);
          }
        } catch (Exception e) {
          log.error("Error fetching match {}: {}", matchId, e.getMessage());
          // Continue with other matches
        }
      }

      log.info("Successfully retrieved {} match summaries", summaries.size());
      return summaries;
    } catch (Exception e) {
      log.error("Error fetching recent matches for PUUID: {}", puuid, e);
      throw new RuntimeException("Failed to fetch recent matches for PUUID: " + puuid, e);
    }
  }

  public MatchDetails getMatchDetails(String matchId, String puuid) {
    try {
      log.info("Fetching match details for match ID: {} and PUUID: {}", matchId, puuid);

      MatchData matchData = getMatchById(matchId);

      MatchDetails details = new MatchDetails();
      details.setMatchId(matchData.getMetadata().getMatchId());
      details.setGameDuration(matchData.getInfo().getGameDuration());
      details.setGameCreation(matchData.getInfo().getGameCreation());

      // Convert all participants to PlayerStats
      List<MatchDetails.PlayerStats> allPlayers = matchData.getInfo().getParticipants()
        .stream()
        .map(this::convertToPlayerStats)
        .collect(Collectors.toList());

      details.setAllPlayers(allPlayers);

      // Find and set the specific player's stats
      MatchDetails.PlayerStats playerStats = allPlayers.stream()
        .filter(p -> p.getPuuid().equals(puuid))
        .findFirst()
        .orElse(null);

      details.setPlayerStats(playerStats);

      log.info("Successfully retrieved match details");
      return details;
    } catch (Exception e) {
      log.error("Error fetching match details for match ID: {}", matchId, e);
      throw new RuntimeException("Failed to fetch match details for match: " + matchId, e);
    }
  }

  private MatchDetails.PlayerStats convertToPlayerStats(MatchData.ParticipantDto participant) {
    MatchDetails.PlayerStats stats = new MatchDetails.PlayerStats();
    stats.setPuuid(participant.getPuuid());
    stats.setSummonerName(participant.getSummonerName());
    stats.setRiotIdGameName(participant.getRiotIdGameName());
    stats.setRiotIdTagline(participant.getRiotIdTagline());
    stats.setChampionName(participant.getChampionName());
    stats.setKills(participant.getKills());
    stats.setDeaths(participant.getDeaths());
    stats.setAssists(participant.getAssists());
    stats.setWin(participant.isWin());
    stats.setGoldEarned(participant.getGoldEarned());
    stats.setVisionScore(participant.getVisionScore());
    stats.setTotalMinionsKilled(participant.getTotalMinionsKilled());
    stats.setNeutralMinionsKilled(participant.getNeutralMinionsKilled());
    stats.setDoubleKills(participant.getDoubleKills());
    stats.setTripleKills(participant.getTripleKills());
    stats.setQuadraKills(participant.getQuadraKills());
    stats.setPentaKills(participant.getPentaKills());
    stats.setLane(participant.getLane());

    // Add summoner spell IDs
    stats.setSummoner1Id(participant.getSummoner1Id());
    stats.setSummoner2Id(participant.getSummoner2Id());

    // Add champion avatar URL using Data Dragon
    String championAvatarUrl = String.format(
      "https://ddragon.leagueoflegends.com/cdn/14.23.1/img/champion/%s.png",
      participant.getChampionName()
    );
    stats.setChampionAvatarUrl(championAvatarUrl);

    return stats;
  }

  public RadarChartStats getRadarChartStats(String matchId, String puuid) {
    try {
      log.info("Fetching radar chart stats for match ID: {} and PUUID: {}", matchId, puuid);

      MatchData matchData = getMatchById(matchId);

      // Find the player's participant data
      MatchData.ParticipantDto player = matchData.getInfo().getParticipants()
        .stream()
        .filter(p -> p.getPuuid().equals(puuid))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Player not found in match: " + matchId));

      // Find the opponent in the same position (opposite team)
      String playerPosition = getBestPosition(player);
      int playerTeam = getTeamId(matchData, puuid);

      log.info("Player position: {}, Team: {}", playerPosition, playerTeam);

      MatchData.ParticipantDto opponent = matchData.getInfo().getParticipants()
        .stream()
        .filter(p -> !p.getPuuid().equals(puuid))
        .filter(p -> getTeamId(matchData, p.getPuuid()) != playerTeam)
        .filter(p -> {
          String opponentPosition = getBestPosition(p);
          return opponentPosition.equals(playerPosition);
        })
        .findFirst()
        .orElse(null);

      // If no opponent found in same position, just get first enemy
      if (opponent == null) {
        log.warn("No direct lane opponent found for position {}, selecting first opponent from enemy team", playerPosition);
        opponent = matchData.getInfo().getParticipants()
          .stream()
          .filter(p -> !p.getPuuid().equals(puuid))
          .filter(p -> getTeamId(matchData, p.getPuuid()) != playerTeam)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("No opponent found in match"));
      }

      // Get game duration in minutes
      double gameDurationMinutes = matchData.getInfo().getGameDuration() / 60.0;

      RadarChartStats.PlayerRadarStats playerStats = convertToRadarStats(player, gameDurationMinutes, playerPosition);
      RadarChartStats.PlayerRadarStats opponentStats = convertToRadarStats(opponent, gameDurationMinutes, getBestPosition(opponent));

      // Normalize stats based on rank/role benchmarks
      normalizeRadarStatsWithBenchmarks(playerStats, opponentStats);

      RadarChartStats radarStats = new RadarChartStats(matchId, playerStats, opponentStats);

      log.info("Successfully created radar chart stats");
      return radarStats;
    } catch (Exception e) {
      log.error("Error fetching radar chart stats for match ID: {}", matchId, e);
      throw new RuntimeException("Failed to fetch radar chart stats for match: " + matchId, e);
    }
  }

  private RadarChartStats.PlayerRadarStats convertToRadarStats(MatchData.ParticipantDto participant, double gameDurationMinutes, String position) {
    RadarChartStats.PlayerRadarStats stats = new RadarChartStats.PlayerRadarStats();

    stats.setPuuid(participant.getPuuid());
    stats.setSummonerName(participant.getRiotIdGameName() + "#" + participant.getRiotIdTagline());
    stats.setChampionName(participant.getChampionName());
    stats.setLane(position); // Use the resolved position instead of just lane
    stats.setWin(participant.isWin());

    // Raw values
    stats.setTotalDamageDealtToChampions(participant.getTotalDamageDealtToChampions());
    stats.setTotalDamageTaken(participant.getTotalDamageTaken());
    stats.setTotalMinionsKilled(participant.getTotalMinionsKilled());
    stats.setNeutralMinionsKilled(participant.getNeutralMinionsKilled());
    stats.setGoldEarned(participant.getGoldEarned());
    stats.setVisionScore(participant.getVisionScore());
    stats.setWardsPlaced(participant.getWardsPlaced());
    stats.setWardsKilled(participant.getWardsKilled());
    stats.setKills(participant.getKills());
    stats.setDeaths(participant.getDeaths());
    stats.setAssists(participant.getAssists());

    // Calculate KDA
    double kda = participant.getDeaths() == 0
      ? participant.getKills() + participant.getAssists()
      : (double) (participant.getKills() + participant.getAssists()) / participant.getDeaths();
    stats.setKda(kda);

    // Calculate per-minute stats (will be normalized later against benchmarks)
    if (gameDurationMinutes > 0) {
      stats.setDamage(participant.getTotalDamageDealtToChampions() / gameDurationMinutes);
      stats.setDamageTaken(participant.getTotalDamageTaken() / gameDurationMinutes);
      stats.setFarm((participant.getTotalMinionsKilled() + participant.getNeutralMinionsKilled()) / gameDurationMinutes);
      stats.setGold(participant.getGoldEarned() / gameDurationMinutes);
      stats.setVision(participant.getWardsPlaced() / gameDurationMinutes); // Use wards/min for vision
    }

    return stats;
  }

  private void normalizeRadarStatsWithBenchmarks(RadarChartStats.PlayerRadarStats player, RadarChartStats.PlayerRadarStats opponent) {
    // Normalize stats to 0-100 scale based on rank/role benchmarks
    // 100 = at benchmark, 200 = 2x benchmark, 50 = 0.5x benchmark

    // Detect roles
    PerformanceBenchmarks.Role playerRole = PerformanceBenchmarks.detectRole(player.getLane());
    PerformanceBenchmarks.Role opponentRole = PerformanceBenchmarks.detectRole(opponent.getLane());

    // Use Gold rank as default (could be enhanced to detect actual rank)
    PerformanceBenchmarks.Rank rank = PerformanceBenchmarks.getDefaultRank();

    // Get benchmarks
    PerformanceBenchmarks.Benchmark playerBenchmark = PerformanceBenchmarks.getBenchmark(rank, playerRole);
    PerformanceBenchmarks.Benchmark opponentBenchmark = PerformanceBenchmarks.getBenchmark(rank, opponentRole);

    // Normalize player stats
    normalizePlayerStats(player, playerBenchmark);
    normalizePlayerStats(opponent, opponentBenchmark);
  }

  private void normalizePlayerStats(RadarChartStats.PlayerRadarStats stats, PerformanceBenchmarks.Benchmark benchmark) {
    // Normalize each stat: (actual / benchmark) * 100
    // No cap - let stats overflow if they're exceptional

    if (benchmark.damagePerMin > 0) {
      stats.setDamage((stats.getDamage() / benchmark.damagePerMin) * 100);
    }

    if (benchmark.goldPerMin > 0) {
      stats.setGold((stats.getGold() / benchmark.goldPerMin) * 100);
    }

    if (benchmark.csPerMin > 0) {
      stats.setFarm((stats.getFarm() / benchmark.csPerMin) * 100);
    }

    if (benchmark.wardsPerMin > 0) {
      stats.setVision((stats.getVision() / benchmark.wardsPerMin) * 100);
    }

    if (benchmark.kda > 0) {
      stats.setKda((stats.getKda() / benchmark.kda) * 100);
    }

    // Damage Taken: normalize similarly (higher damage taken = more frontline presence)
    double avgDamageTaken = 450; // Rough average across roles
    stats.setDamageTaken((stats.getDamageTaken() / avgDamageTaken) * 100);
  }

  /**
   * Get the best position field from participant data.
   * Prefers teamPosition > individualPosition > lane
   */
  private String getBestPosition(MatchData.ParticipantDto participant) {
    // Try teamPosition first (most reliable in recent patches)
    if (participant.getTeamPosition() != null && !participant.getTeamPosition().isEmpty()
        && !participant.getTeamPosition().equals("Invalid") && !participant.getTeamPosition().equals("NONE")) {
      return participant.getTeamPosition();
    }

    // Try individualPosition
    if (participant.getIndividualPosition() != null && !participant.getIndividualPosition().isEmpty()
        && !participant.getIndividualPosition().equals("Invalid") && !participant.getIndividualPosition().equals("NONE")) {
      return participant.getIndividualPosition();
    }

    // Fallback to lane (deprecated but still exists)
    if (participant.getLane() != null && !participant.getLane().isEmpty()
        && !participant.getLane().equals("NONE")) {
      return participant.getLane();
    }

    // Ultimate fallback
    return "UNKNOWN";
  }

  private int getTeamId(MatchData matchData, String puuid) {
    // In League of Legends, participants 0-4 are team 100, participants 5-9 are team 200
    List<MatchData.ParticipantDto> participants = matchData.getInfo().getParticipants();
    for (int i = 0; i < participants.size(); i++) {
      if (participants.get(i).getPuuid().equals(puuid)) {
        return i < 5 ? 100 : 200;
      }
    }
    throw new RuntimeException("Player not found in match");
  }

}
