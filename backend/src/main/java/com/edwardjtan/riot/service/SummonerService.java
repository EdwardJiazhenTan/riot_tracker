package com.edwardjtan.riot.service;

import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.merakianalytics.orianna.types.common.Platform;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class SummonerService {

  private static final Logger log = LoggerFactory.getLogger(SummonerService.class);
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${riot.api.key}")
  private String apiKey;

  public String getPuuidByRiotId(String gameName, String tagLine) {
    try {
      String url = String.format(
        "https://americas.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s",
        gameName, tagLine, apiKey
      );

      log.info("Fetching PUUID for {}#{}", gameName, tagLine);
      JsonNode response = restTemplate.getForObject(url, JsonNode.class);

      if (response != null && response.has("puuid")) {
        String puuid = response.get("puuid").asText();
        log.info("Found PUUID: {}", puuid);
        return puuid;
      }

      throw new RuntimeException("Could not find PUUID in response");
    } catch (Exception e) {
      log.error("Error fetching PUUID for {}#{}", gameName, tagLine, e);
      throw new RuntimeException("Failed to fetch account: " + gameName + "#" + tagLine, e);
    }
  }

  public Summoner getSummonerByPuuid(String puuid) {
    try {
      log.info("Fetching summoner by PUUID: {}", puuid);
      return Summoner.withPuuid(puuid).withPlatform(Platform.NORTH_AMERICA).get();
    } catch (Exception e) {
      log.error("Error fetching summoner by PUUID: {}", puuid, e);
      throw new RuntimeException("Failed to fetch summoner by PUUID: " + puuid, e);
    }
  }

  public Summoner getSummonerByNameAndTag(String gameName, String tagLine) {
    try {
      String puuid = getPuuidByRiotId(gameName, tagLine);
      return getSummonerByPuuid(puuid);
    } catch (Exception e) {
      log.error("Error fetching summoner: {}#{}", gameName, tagLine, e);
      throw new RuntimeException("Failed to fetch summoner: " + gameName + "#" + tagLine, e);
    }
  }

  public Summoner getSummonerByName(String summonerName) {
    try {
      log.info("Fetching summoner: {}", summonerName);
      return Summoner.named(summonerName).get();
    } catch (Exception e) {
      log.error("Error fetching summoner: {}", summonerName, e);
      throw e;
    }
  }

  public String getSummonerInfo(String summonerName) {
    var summoner = getSummonerByName(summonerName);
    return "Summoner: " + summoner.getName() +
           ", Level: " + summoner.getLevel() +
           ", ID: " + summoner.getId();
  }

  public String getSummonerInfoByNameAndTag(String gameName, String tagLine) {
    var summoner = getSummonerByNameAndTag(gameName, tagLine);
    return "Summoner: " + summoner.getName() +
           ", Level: " + summoner.getLevel() +
           ", ID: " + summoner.getId() +
           ", PUUID: " + summoner.getPuuid();
  }

}
