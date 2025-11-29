# Riot Game Tracker with Claude AI

A Spring Boot application that tracks League of Legends games and generates AI-powered game reports using Claude.

## Features

- Fetch player information by summoner name
- Retrieve the latest match for a player
- Extract important game events (kills, deaths, assists, multi-kills)
- Generate detailed game analysis reports using Claude AI

## Note
The `tagLine` parameter is currently used for display purposes. Due to Orianna 4.0.0-rc8 limitations, the actual lookup uses the summoner `gameName` only. For best results, use the exact in-game summoner name.

## Prerequisites

- Java 25
- Maven
- Riot Games API Key
- Anthropic API Key

## Setup

1. Set your API keys as environment variables:
```bash
export RIOT_API_KEY=your_riot_api_key
export ANTHROPIC_API_KEY=your_anthropic_api_key
```

2. Build the project:
```bash
./mvnw clean install
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Get Game Report (Query Parameters)
```
GET /api/game-tracker/report?gameName=Tekindar666&tagLine=8848
```

### Get Game Report (Path Parameters)
```
GET /api/game-tracker/report/Tekindar666/8848
```

## Example Usage

Using curl:
```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

Or:
```bash
curl "http://localhost:8080/api/game-tracker/report/Tekindar666/8848"
```

## Testing

Run the test for Tekindar666#8848:
```bash
./mvnw test -Dtest=GameTrackerServiceTest
```

## How It Works

1. **Summoner Lookup**: Fetches player PUUID using Riot ID (gameName#tagLine)
2. **Match Retrieval**: Gets the most recent match from match history
3. **Event Extraction**: Analyzes match timeline to extract:
   - Kills, deaths, and assists
   - Objective takedowns (dragons, barons, towers)
   - Special kills (double, triple, quadra, penta)
   - Player statistics (damage, gold, vision score, CS)
4. **AI Analysis**: Sends trimmed data to Claude API for professional game report generation
5. **Report Generation**: Returns a comprehensive analysis with:
   - Performance summary
   - Key strengths and highlights
   - Areas for improvement
   - Notable moments
   - Recommendations

## Project Structure

```
src/main/java/com/edwardjtan/riot/
├── controller/
│   └── GameTrackerController.java      # REST API endpoints
├── service/
│   ├── SummonerService.java           # Player lookup
│   ├── MatchService.java              # Match retrieval
│   ├── GameAnalysisService.java       # Event extraction
│   ├── ClaudeService.java             # AI report generation
│   └── GameTrackerService.java        # Main orchestration
├── model/
│   ├── GameEvent.java                 # Event data model
│   └── GameAnalysis.java              # Analysis data model
└── config/
    └── OriannaConfig.java             # Orianna configuration
```

## Notes

- Currently configured for North America (NA) server
- Uses Orianna 4.0.0-rc8 for Riot API integration
- Uses Claude Sonnet 4.5 for game analysis
