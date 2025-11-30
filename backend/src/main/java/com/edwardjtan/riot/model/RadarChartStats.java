package com.edwardjtan.riot.model;

public class RadarChartStats {

  private String matchId;
  private PlayerRadarStats playerStats;
  private PlayerRadarStats opponentStats;

  public RadarChartStats() {}

  public RadarChartStats(String matchId, PlayerRadarStats playerStats, PlayerRadarStats opponentStats) {
    this.matchId = matchId;
    this.playerStats = playerStats;
    this.opponentStats = opponentStats;
  }

  public String getMatchId() {
    return matchId;
  }

  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }

  public PlayerRadarStats getPlayerStats() {
    return playerStats;
  }

  public void setPlayerStats(PlayerRadarStats playerStats) {
    this.playerStats = playerStats;
  }

  public PlayerRadarStats getOpponentStats() {
    return opponentStats;
  }

  public void setOpponentStats(PlayerRadarStats opponentStats) {
    this.opponentStats = opponentStats;
  }

  public static class PlayerRadarStats {
    private String puuid;
    private String summonerName;
    private String championName;
    private String lane;
    private boolean win;

    // Six stats for the radar chart
    private double damage;          // Total damage dealt to champions
    private double damageTaken;     // Total damage taken
    private double farm;            // Total CS (minions + neutral)
    private double gold;            // Gold earned
    private double vision;          // Vision score
    private double kda;             // (Kills + Assists) / Deaths (or kills+assists if deaths=0)

    // Raw values for reference
    private int totalDamageDealtToChampions;
    private int totalDamageTaken;
    private int totalMinionsKilled;
    private int neutralMinionsKilled;
    private int goldEarned;
    private int visionScore;
    private int wardsPlaced;
    private int wardsKilled;
    private int kills;
    private int deaths;
    private int assists;

    public PlayerRadarStats() {}

    // Getters and setters
    public String getPuuid() {
      return puuid;
    }

    public void setPuuid(String puuid) {
      this.puuid = puuid;
    }

    public String getSummonerName() {
      return summonerName;
    }

    public void setSummonerName(String summonerName) {
      this.summonerName = summonerName;
    }

    public String getChampionName() {
      return championName;
    }

    public void setChampionName(String championName) {
      this.championName = championName;
    }

    public String getLane() {
      return lane;
    }

    public void setLane(String lane) {
      this.lane = lane;
    }

    public boolean isWin() {
      return win;
    }

    public void setWin(boolean win) {
      this.win = win;
    }

    public double getDamage() {
      return damage;
    }

    public void setDamage(double damage) {
      this.damage = damage;
    }

    public double getDamageTaken() {
      return damageTaken;
    }

    public void setDamageTaken(double damageTaken) {
      this.damageTaken = damageTaken;
    }

    public double getFarm() {
      return farm;
    }

    public void setFarm(double farm) {
      this.farm = farm;
    }

    public double getGold() {
      return gold;
    }

    public void setGold(double gold) {
      this.gold = gold;
    }

    public double getVision() {
      return vision;
    }

    public void setVision(double vision) {
      this.vision = vision;
    }

    public double getKda() {
      return kda;
    }

    public void setKda(double kda) {
      this.kda = kda;
    }

    public int getTotalDamageDealtToChampions() {
      return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(int totalDamageDealtToChampions) {
      this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public int getTotalDamageTaken() {
      return totalDamageTaken;
    }

    public void setTotalDamageTaken(int totalDamageTaken) {
      this.totalDamageTaken = totalDamageTaken;
    }

    public int getTotalMinionsKilled() {
      return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
      this.totalMinionsKilled = totalMinionsKilled;
    }

    public int getNeutralMinionsKilled() {
      return neutralMinionsKilled;
    }

    public void setNeutralMinionsKilled(int neutralMinionsKilled) {
      this.neutralMinionsKilled = neutralMinionsKilled;
    }

    public int getGoldEarned() {
      return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
      this.goldEarned = goldEarned;
    }

    public int getVisionScore() {
      return visionScore;
    }

    public void setVisionScore(int visionScore) {
      this.visionScore = visionScore;
    }

    public int getWardsPlaced() {
      return wardsPlaced;
    }

    public void setWardsPlaced(int wardsPlaced) {
      this.wardsPlaced = wardsPlaced;
    }

    public int getWardsKilled() {
      return wardsKilled;
    }

    public void setWardsKilled(int wardsKilled) {
      this.wardsKilled = wardsKilled;
    }

    public int getKills() {
      return kills;
    }

    public void setKills(int kills) {
      this.kills = kills;
    }

    public int getDeaths() {
      return deaths;
    }

    public void setDeaths(int deaths) {
      this.deaths = deaths;
    }

    public int getAssists() {
      return assists;
    }

    public void setAssists(int assists) {
      this.assists = assists;
    }
  }
}
