package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.service.SummonerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/summoner")
class SummonerController {

  private final SummonerService summonerService;

  SummonerController(SummonerService summonerService) {
    this.summonerService = summonerService;
  }

  @GetMapping("/{name}/{tag}")
  String getSummoner(@PathVariable String name, @PathVariable String tag) {
    return summonerService.getSummonerInfoByNameAndTag(name, tag);
  }

  @GetMapping("/{name}/{tag}/puuid")
  Map<String, String> getPuuid(@PathVariable String name, @PathVariable String tag) {
    String puuid = summonerService.getPuuidByRiotId(name, tag);
    return Map.of("puuid", puuid);
  }

}
