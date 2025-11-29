package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.MatchData;
import com.edwardjtan.riot.model.MatchSummary;
import com.edwardjtan.riot.model.MatchDetails;
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

}
