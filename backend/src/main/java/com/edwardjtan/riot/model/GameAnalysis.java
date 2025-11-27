package com.edwardjtan.riot.model;

import java.util.List;
import java.util.Map;

public class GameAnalysis {
  private final String matchId;
  private final String playerPuuid;
  private final String playerName;
  private final String champion;
  private final String role;
  private final boolean victory;
  private final int kills;
  private final int deaths;
  private final int assists;
  private final long gameDuration;
  private final List<GameEvent> importantEvents;
  private final Map<String, Object> stats;

  public GameAnalysis(String matchId, String playerPuuid, String playerName, String champion,
                      String role, boolean victory, int kills, int deaths, int assists,
                      long gameDuration, List<GameEvent> importantEvents, Map<String, Object> stats) {
    this.matchId = matchId;
    this.playerPuuid = playerPuuid;
    this.playerName = playerName;
    this.champion = champion;
    this.role = role;
    this.victory = victory;
    this.kills = kills;
    this.deaths = deaths;
    this.assists = assists;
    this.gameDuration = gameDuration;
    this.importantEvents = importantEvents;
    this.stats = stats;
  }

  public String getMatchId() {
    return matchId;
  }

  public String getPlayerPuuid() {
    return playerPuuid;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getChampion() {
    return champion;
  }

  public String getRole() {
    return role;
  }

  public boolean isVictory() {
    return victory;
  }

  public int getKills() {
    return kills;
  }

  public int getDeaths() {
    return deaths;
  }

  public int getAssists() {
    return assists;
  }

  public long getGameDuration() {
    return gameDuration;
  }

  public List<GameEvent> getImportantEvents() {
    return importantEvents;
  }

  public Map<String, Object> getStats() {
    return stats;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Match: %s\n", matchId));
    sb.append(String.format("Player: %s (%s)\n", playerName, champion));
    sb.append(String.format("Result: %s\n", victory ? "VICTORY" : "DEFEAT"));
    sb.append(String.format("KDA: %d/%d/%d\n", kills, deaths, assists));
    sb.append(String.format("Duration: %d:%02d\n", gameDuration / 60, gameDuration % 60));
    sb.append("\nImportant Events:\n");
    for (GameEvent event : importantEvents) {
      sb.append("  ").append(event.toString()).append("\n");
    }
    return sb.toString();
  }
}
