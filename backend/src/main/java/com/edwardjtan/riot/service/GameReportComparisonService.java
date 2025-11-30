package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.APIComparisonResult;
import com.edwardjtan.riot.model.GameAnalysis;
import com.edwardjtan.riot.model.GameReportComparison;
import com.edwardjtan.riot.model.MatchData;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameReportComparisonService {

    private static final Logger log = LoggerFactory.getLogger(GameReportComparisonService.class);

    private final SummonerService summonerService;
    private final MatchService matchService;
    private final GameAnalysisService gameAnalysisService;
    private final APIComparisonService apiComparisonService;

    public GameReportComparisonService(SummonerService summonerService,
                                      MatchService matchService,
                                      GameAnalysisService gameAnalysisService,
                                      APIComparisonService apiComparisonService) {
        this.summonerService = summonerService;
        this.matchService = matchService;
        this.gameAnalysisService = gameAnalysisService;
        this.apiComparisonService = apiComparisonService;
    }

    /**
     * Generate game reports using multiple AI providers and compare them
     */
    public List<GameReportComparison> compareGameReports(String gameName, String tagLine,
                                                         String anthropicModel, String openaiModel) {
        try {
            log.info("Starting game report comparison for {}#{}", gameName, tagLine);

            // Get game data
            String puuid = summonerService.getPuuidByRiotId(gameName, tagLine);
            MatchData latestMatch = matchService.getLatestMatchByPuuid(puuid);
            GameAnalysis analysis = gameAnalysisService.analyzeMatch(latestMatch, puuid);

            return compareGameReportsByAnalysis(analysis, gameName, tagLine, anthropicModel, openaiModel);

        } catch (Exception e) {
            log.error("Error comparing game reports for {}#{}", gameName, tagLine, e);
            throw new RuntimeException("Failed to compare game reports: " + e.getMessage(), e);
        }
    }

    /**
     * Generate game reports using multiple AI providers and compare them by match ID
     */
    public List<GameReportComparison> compareGameReportsByMatchId(String matchId,
                                                                   String puuid,
                                                                   String anthropicModel,
                                                                   String openaiModel) {
        try {
            log.info("Starting game report comparison for match ID: {} and PUUID: {}", matchId, puuid);

            // Get match data by ID
            MatchData matchData = matchService.getMatchById(matchId);

            GameAnalysis analysis = gameAnalysisService.analyzeMatch(matchData, puuid);

            return compareGameReportsByAnalysis(analysis, null, null, anthropicModel, openaiModel);

        } catch (Exception e) {
            log.error("Error comparing game reports for match ID: {}", matchId, e);
            throw new RuntimeException("Failed to compare game reports: " + e.getMessage(), e);
        }
    }

    /**
     * Internal method to perform the actual comparison based on GameAnalysis
     */
    private List<GameReportComparison> compareGameReportsByAnalysis(GameAnalysis analysis,
                                                                     String gameName,
                                                                     String tagLine,
                                                                     String anthropicModel,
                                                                     String openaiModel) {
        try {
            // Build the prompt for AI providers
            String prompt = buildGameAnalysisPrompt(analysis);

            // Call API comparison service
            List<APIComparisonResult> apiResults = apiComparisonService.compareProviders(
                prompt, anthropicModel, openaiModel
            );

            // Convert to GameReportComparison
            List<GameReportComparison> comparisons = new ArrayList<>();
            for (APIComparisonResult result : apiResults) {
                GameReportComparison comparison = new GameReportComparison();
                comparison.setProvider(result.getProvider());
                comparison.setModel(result.getModel());
                comparison.setSuccess(result.isSuccess());
                comparison.setReport(result.getResponse());
                comparison.setError(result.getError());
                comparison.setResponseTime(result.getResponseTime());
                comparison.setInputTokens(result.getInputTokens());
                comparison.setOutputTokens(result.getOutputTokens());
                comparison.setTotalTokens(result.getTotalTokens());
                comparison.setTimestamp(result.getTimestamp());
                comparison.setGameName(gameName);
                comparison.setTagLine(tagLine);
                comparison.setMatchId(analysis.getMatchId());

                comparisons.add(comparison);
            }

            log.info("Completed game report comparison with {} providers", comparisons.size());
            return comparisons;

        } catch (Exception e) {
            log.error("Error in game report comparison", e);
            throw new RuntimeException("Failed to compare game reports: " + e.getMessage(), e);
        }
    }

    private String buildGameAnalysisPrompt(GameAnalysis analysis) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a professional League of Legends analyst. Generate a detailed game report based on the following match data:\n\n");
        prompt.append("Match ID: ").append(analysis.getMatchId()).append("\n");
        prompt.append("Player: ").append(analysis.getPlayerName()).append("\n");
        prompt.append("Champion: ").append(analysis.getChampion()).append("\n");
        prompt.append("Role: ").append(analysis.getRole()).append("\n");
        prompt.append("Result: ").append(analysis.isVictory() ? "VICTORY" : "DEFEAT").append("\n");
        prompt.append("KDA: ").append(analysis.getKills()).append("/")
            .append(analysis.getDeaths()).append("/")
            .append(analysis.getAssists()).append("\n");
        prompt.append("Game Duration: ").append(analysis.getGameDuration() / 60)
            .append(" minutes ").append(analysis.getGameDuration() % 60).append(" seconds\n\n");

        prompt.append("Statistics:\n");
        analysis.getStats().forEach((key, value) ->
            prompt.append("- ").append(key).append(": ").append(value).append("\n")
        );

        prompt.append("\nKey Events (").append(analysis.getImportantEvents().size()).append(" total):\n");
        analysis.getImportantEvents().forEach(event ->
            prompt.append("- ").append(event.toString()).append("\n")
        );

        prompt.append("\nPlease provide:\n");
        prompt.append("1. Overall performance summary\n");
        prompt.append("2. Key strengths and highlights from the game\n");
        prompt.append("3. Areas for improvement\n");
        prompt.append("4. Notable moments or plays\n");
        prompt.append("5. Recommendations for future games\n\n");
        prompt.append("Keep the report concise, professional, and actionable.");

        return prompt.toString();
    }
}
