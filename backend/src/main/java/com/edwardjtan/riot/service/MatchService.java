package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.MatchData;
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

}
