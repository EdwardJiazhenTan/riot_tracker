package com.edwardjtan.riot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchData {

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("info")
  private Info info;

  public Metadata getMetadata() {
    return metadata;
  }

  public Info getInfo() {
    return info;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Metadata {
    @JsonProperty("matchId")
    private String matchId;

    public String getMatchId() {
      return matchId;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Info {
    @JsonProperty("gameDuration")
    private long gameDuration;

    @JsonProperty("gameCreation")
    private long gameCreation;

    @JsonProperty("participants")
    private List<ParticipantDto> participants;

    public long getGameDuration() {
      return gameDuration;
    }

    public long getGameCreation() {
      return gameCreation;
    }

    public List<ParticipantDto> getParticipants() {
      return participants;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ParticipantDto {
    @JsonProperty("puuid")
    private String puuid;

    @JsonProperty("championName")
    private String championName;

    @JsonProperty("kills")
    private int kills;

    @JsonProperty("deaths")
    private int deaths;

    @JsonProperty("assists")
    private int assists;

    @JsonProperty("win")
    private boolean win;

    @JsonProperty("goldEarned")
    private int goldEarned;

    @JsonProperty("visionScore")
    private int visionScore;

    @JsonProperty("totalMinionsKilled")
    private int totalMinionsKilled;

    @JsonProperty("neutralMinionsKilled")
    private int neutralMinionsKilled;

    @JsonProperty("doubleKills")
    private int doubleKills;

    @JsonProperty("tripleKills")
    private int tripleKills;

    @JsonProperty("quadraKills")
    private int quadraKills;

    @JsonProperty("pentaKills")
    private int pentaKills;

    @JsonProperty("lane")
    private String lane;

    @JsonProperty("summonerName")
    private String summonerName;

    @JsonProperty("riotIdGameName")
    private String riotIdGameName;

    @JsonProperty("riotIdTagline")
    private String riotIdTagline;

    @JsonProperty("summoner1Id")
    private int summoner1Id;

    @JsonProperty("summoner2Id")
    private int summoner2Id;

    @JsonProperty("summoner1Castss")
    private int summoner1Casts;

    @JsonProperty("summoner2Casts")
    private int summoner2Casts;

    @JsonProperty("totalDamageDealtToChampions")
    private int totalDamageDealtToChampions;

    @JsonProperty("totalDamageTaken")
    private int totalDamageTaken;

    @JsonProperty("wardsPlaced")
    private int wardsPlaced;

    @JsonProperty("wardsKilled")
    private int wardsKilled;

    // Getters
    public String getPuuid() { return puuid; }
    public String getChampionName() { return championName; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getAssists() { return assists; }
    public boolean isWin() { return win; }
    public int getGoldEarned() { return goldEarned; }
    public int getVisionScore() { return visionScore; }
    public int getTotalMinionsKilled() { return totalMinionsKilled; }
    public int getNeutralMinionsKilled() { return neutralMinionsKilled; }
    public int getDoubleKills() { return doubleKills; }
    public int getTripleKills() { return tripleKills; }
    public int getQuadraKills() { return quadraKills; }
    public int getPentaKills() { return pentaKills; }
    public String getLane() { return lane; }
    public String getSummonerName() { return summonerName; }
    public String getRiotIdGameName() { return riotIdGameName; }
    public String getRiotIdTagline() { return riotIdTagline; }
    public int getSummoner1Id() { return summoner1Id; }
    public int getSummoner2Id() { return summoner2Id; }
    public int getSummoner1Casts() { return summoner1Casts; }
    public int getSummoner2Casts() { return summoner2Casts; }
    public int getTotalDamageDealtToChampions() { return totalDamageDealtToChampions; }
    public int getTotalDamageTaken() { return totalDamageTaken; }
    public int getWardsPlaced() { return wardsPlaced; }
    public int getWardsKilled() { return wardsKilled; }
  }
}
