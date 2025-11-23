package com.edwardjtan.riot.config;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
class OriannaConfig {

  @Value("${riot.api.key}")
  private String apiKey;

  @PostConstruct
  void initialize() {
    Orianna.setRiotAPIKey(apiKey);
    Orianna.setDefaultRegion(Region.NORTH_AMERICA);
  }

}
