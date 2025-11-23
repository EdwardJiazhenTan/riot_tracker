package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.GameAnalysis;
import com.edwardjtan.riot.model.GameEvent;
import com.edwardjtan.riot.model.MatchData;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameAnalysisService {

  private static final Logger log = LoggerFactory.getLogger(GameAnalysisService.class);

  public GameAnalysis analyzeMatch(MatchData matchData, String puuid) {
    try {
      log.info("Analyzing match {} for PUUID {}", matchData.getMetadata().getMatchId(), puuid);
      log.info("Total participants in match: {}", matchData.getInfo().getParticipants().size());

      // Find the player's participant data
      MatchData.ParticipantDto playerParticipant = matchData.getInfo().getParticipants().stream()
        .filter(p -> p.getPuuid().equals(puuid))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Player with PUUID " + puuid + " not found in match"));

      log.info("Found player: {}", playerParticipant.getRiotIdGameName());

      List<GameEvent> importantEvents = extractImportantEvents(playerParticipant);

      Map<String, Object> stats = new HashMap<>();
      stats.put("goldEarned", playerParticipant.getGoldEarned());
      stats.put("visionScore", playerParticipant.getVisionScore());
      stats.put("totalMinionsKilled", playerParticipant.getTotalMinionsKilled() + playerParticipant.getNeutralMinionsKilled());

      String playerName = playerParticipant.getRiotIdGameName() != null ?
        playerParticipant.getRiotIdGameName() + "#" + playerParticipant.getRiotIdTagline() :
        playerParticipant.getSummonerName();

      String role = playerParticipant.getLane() != null ? playerParticipant.getLane() : "UNKNOWN";

      return new GameAnalysis(
        matchData.getMetadata().getMatchId(),
        puuid,
        playerName,
        playerParticipant.getChampionName(),
        role,
        playerParticipant.isWin(),
        playerParticipant.getKills(),
        playerParticipant.getDeaths(),
        playerParticipant.getAssists(),
        matchData.getInfo().getGameDuration(),
        importantEvents,
        stats
      );
    } catch (Exception e) {
      log.error("Error analyzing match", e);
      throw new RuntimeException("Failed to analyze match", e);
    }
  }

  private List<GameEvent> extractImportantEvents(MatchData.ParticipantDto player) {
    List<GameEvent> events = new ArrayList<>();
    String playerName = player.getRiotIdGameName() != null ?
      player.getRiotIdGameName() : player.getSummonerName();

    try {
      int kills = player.getKills();
      int deaths = player.getDeaths();
      int assists = player.getAssists();

      if (kills > 0) {
        events.add(new GameEvent(
          "KILLS",
          0,
          kills + " total kills",
          playerName
        ));
      }

      if (deaths > 0) {
        events.add(new GameEvent(
          "DEATHS",
          0,
          deaths + " total deaths",
          playerName
        ));
      }

      if (assists > 0) {
        events.add(new GameEvent(
          "ASSISTS",
          0,
          assists + " total assists",
          playerName
        ));
      }

      if (player.getDoubleKills() > 0) {
        events.add(new GameEvent(
          "SPECIAL",
          0,
          player.getDoubleKills() + " double kills",
          playerName
        ));
      }

      if (player.getTripleKills() > 0) {
        events.add(new GameEvent(
          "SPECIAL",
          0,
          player.getTripleKills() + " triple kills",
          playerName
        ));
      }

      if (player.getQuadraKills() > 0) {
        events.add(new GameEvent(
          "SPECIAL",
          0,
          player.getQuadraKills() + " quadra kills",
          playerName
        ));
      }

      if (player.getPentaKills() > 0) {
        events.add(new GameEvent(
          "SPECIAL",
          0,
          player.getPentaKills() + " penta kills",
          playerName
        ));
      }

      log.info("Extracted {} important events", events.size());
    } catch (Exception e) {
      log.error("Error extracting events", e);
    }

    return events;
  }
}
