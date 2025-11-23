package com.edwardjtan.riot.controller;

import com.edwardjtan.riot.service.SummonerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summoner")
class SummonerController {

  private final SummonerService summonerService;

  SummonerController(SummonerService summonerService) {
    this.summonerService = summonerService;
  }

  @GetMapping("/{name}")
  String getSummoner(@PathVariable String name) {
    return summonerService.getSummonerInfo(name);
  }

}
