package com.edwardjtan.demo;

import com.edwardjtan.riot.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameTrackerServiceTest {

  private static final Logger log = LoggerFactory.getLogger(GameTrackerServiceTest.class);

  @Autowired
  private GameTrackerService gameTrackerService;

  @Test
  public void testGenerateGameReportForTekindar666() {
    String gameName = "Tekindar666";
    String tagLine = "8848";

    log.info("Testing game report generation for {}#{}", gameName, tagLine);

    try {
      String report = gameTrackerService.generateGameReport(gameName, tagLine);

      assertNotNull(report, "Report should not be null");
      assertFalse(report.isEmpty(), "Report should not be empty");

      log.info("Generated report:\n{}", report);

      assertTrue(report.length() > 100, "Report should be substantial");
    } catch (Exception e) {
      log.error("Test failed with exception", e);
      fail("Should not throw exception: " + e.getMessage());
    }
  }
}
