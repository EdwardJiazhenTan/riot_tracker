package com.edwardjtan.riot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MatchDetails {
    @JsonProperty("matchId")
    private String matchId;

    @JsonProperty("gameDuration")
    private long gameDuration;

    @JsonProperty("gameCreation")
    private long gameCreation;

    @JsonProperty("playerStats")
    private PlayerStats playerStats;

    @JsonProperty("allPlayers")
    private List<PlayerStats> allPlayers;

    // Constructors
    public MatchDetails() {}

    // Getters and Setters
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public List<PlayerStats> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<PlayerStats> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public static class PlayerStats {
        @JsonProperty("puuid")
        private String puuid;

        @JsonProperty("summonerName")
        private String summonerName;

        @JsonProperty("riotIdGameName")
        private String riotIdGameName;

        @JsonProperty("riotIdTagline")
        private String riotIdTagline;

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

        @JsonProperty("summoner1Id")
        private int summoner1Id;

        @JsonProperty("summoner2Id")
        private int summoner2Id;

        @JsonProperty("championAvatarUrl")
        private String championAvatarUrl;

        // Getters and Setters
        public String getPuuid() { return puuid; }
        public void setPuuid(String puuid) { this.puuid = puuid; }

        public String getSummonerName() { return summonerName; }
        public void setSummonerName(String summonerName) { this.summonerName = summonerName; }

        public String getRiotIdGameName() { return riotIdGameName; }
        public void setRiotIdGameName(String riotIdGameName) { this.riotIdGameName = riotIdGameName; }

        public String getRiotIdTagline() { return riotIdTagline; }
        public void setRiotIdTagline(String riotIdTagline) { this.riotIdTagline = riotIdTagline; }

        public String getChampionName() { return championName; }
        public void setChampionName(String championName) { this.championName = championName; }

        public int getKills() { return kills; }
        public void setKills(int kills) { this.kills = kills; }

        public int getDeaths() { return deaths; }
        public void setDeaths(int deaths) { this.deaths = deaths; }

        public int getAssists() { return assists; }
        public void setAssists(int assists) { this.assists = assists; }

        public boolean isWin() { return win; }
        public void setWin(boolean win) { this.win = win; }

        public int getGoldEarned() { return goldEarned; }
        public void setGoldEarned(int goldEarned) { this.goldEarned = goldEarned; }

        public int getVisionScore() { return visionScore; }
        public void setVisionScore(int visionScore) { this.visionScore = visionScore; }

        public int getTotalMinionsKilled() { return totalMinionsKilled; }
        public void setTotalMinionsKilled(int totalMinionsKilled) { this.totalMinionsKilled = totalMinionsKilled; }

        public int getNeutralMinionsKilled() { return neutralMinionsKilled; }
        public void setNeutralMinionsKilled(int neutralMinionsKilled) { this.neutralMinionsKilled = neutralMinionsKilled; }

        public int getDoubleKills() { return doubleKills; }
        public void setDoubleKills(int doubleKills) { this.doubleKills = doubleKills; }

        public int getTripleKills() { return tripleKills; }
        public void setTripleKills(int tripleKills) { this.tripleKills = tripleKills; }

        public int getQuadraKills() { return quadraKills; }
        public void setQuadraKills(int quadraKills) { this.quadraKills = quadraKills; }

        public int getPentaKills() { return pentaKills; }
        public void setPentaKills(int pentaKills) { this.pentaKills = pentaKills; }

        public String getLane() { return lane; }
        public void setLane(String lane) { this.lane = lane; }

        public int getSummoner1Id() { return summoner1Id; }
        public void setSummoner1Id(int summoner1Id) { this.summoner1Id = summoner1Id; }

        public int getSummoner2Id() { return summoner2Id; }
        public void setSummoner2Id(int summoner2Id) { this.summoner2Id = summoner2Id; }

        public String getChampionAvatarUrl() { return championAvatarUrl; }
        public void setChampionAvatarUrl(String championAvatarUrl) { this.championAvatarUrl = championAvatarUrl; }
    }
}
