# Riot Game Tracker with Claude AI

A Spring Boot application that analyzes League of Legends games and generates AI-powered performance reports using Claude.

## Quick Start

```bash
# 1. Setup (creates .env file and builds project)
./setup.sh

# 2. Edit .env and add your API keys
nano .env

# 3. Run the server
./mvnw spring-boot:run

# 4. Test it (in another terminal)
./test-api.sh
```

## What Does It Do?

This app fetches your latest League of Legends match and generates a professional game analysis report powered by Claude AI, including:

- Performance summary
- Key strengths and highlights
- Areas for improvement
- Notable moments and plays
- Recommendations for future games

## API Endpoints

### Get Game Report
```
GET /api/game-tracker/report?gameName=<NAME>&tagLine=<TAG>
```

**Example:**
```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

## Configuration

### Using .env file (Recommended)

1. Copy the example file:
```bash
cp .env.example .env
```

2. Edit `.env` and add your API keys:
```
RIOT_API_KEY=your_riot_api_key_here
ANTHROPIC_API_KEY=your_anthropic_api_key_here
```

### Get API Keys

- **Riot API Key**: https://developer.riotgames.com/
- **Anthropic API Key**: https://console.anthropic.com/

## Requirements

- Java 25
- Maven (included via wrapper)

## Documentation

- [SETUP_GUIDE.md](SETUP_GUIDE.md) - Detailed setup instructions
- [README_GAME_TRACKER.md](README_GAME_TRACKER.md) - Full technical documentation

## Architecture

```
GameTrackerController
    ↓
GameTrackerService
    ↓
    ├── SummonerService → Fetch player
    ├── MatchService → Get latest match
    ├── GameAnalysisService → Extract stats & events
    └── ClaudeService → Generate AI report
```

## Features

- Fetches player information by summoner name
- Retrieves latest match from match history
- Extracts important game events:
  - Kills, deaths, assists (KDA)
  - Multi-kills (double, triple, quadra, penta)
  - Gold earned, vision score
  - Champion, role, game duration
- Generates professional analysis using Claude AI

## Tech Stack

- **Spring Boot 3.5.7** - Application framework
- **Orianna 4.0.0-rc8** - Riot API wrapper
- **Anthropic Java SDK 2.11.1** - Claude AI integration
- **dotenv-java 3.0.0** - .env file support

## Current Limitations

- North America server only
- Uses summoner name (tagLine is for display only)
- Shows latest match only

## Future Enhancements

- Multi-match history analysis
- Support for all regions
- Detailed timeline events (objective timings, team fights)
- Caching to reduce API calls
- Web UI

## License

MIT
