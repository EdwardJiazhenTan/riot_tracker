package com.edwardjtan.riot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchSummary {
    @JsonProperty("matchId")
    private String matchId;

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

    @JsonProperty("gameDuration")
    private long gameDuration;

    @JsonProperty("gameCreation")
    private long gameCreation;

    // Constructors
    public MatchSummary() {}

    public MatchSummary(String matchId, String championName, int kills, int deaths, int assists,
                        boolean win, long gameDuration, long gameCreation) {
        this.matchId = matchId;
        this.championName = championName;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.win = win;
        this.gameDuration = gameDuration;
        this.gameCreation = gameCreation;
    }

    // Getters and Setters
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
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

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }

    public long getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(long gameCreation) {
        this.gameCreation = gameCreation;
    }
}
