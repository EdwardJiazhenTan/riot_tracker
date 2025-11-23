# Quick Setup Guide

## Build Status
✅ Build successful!

## Prerequisites

1. Java 25
2. Riot Games API Key (get from https://developer.riotgames.com/)
3. Anthropic API Key (get from https://console.anthropic.com/)

## Setup Steps

### 1. Set Environment Variables

**Option A: Using .env file (Recommended)**

```bash
# Copy the example file
cp .env.example .env

# Edit .env and add your API keys
nano .env
```

Your `.env` file should look like:
```
RIOT_API_KEY=your_riot_api_key_here
ANTHROPIC_API_KEY=your_anthropic_api_key_here
```

**Option B: Using system environment variables**

```bash
export RIOT_API_KEY=your_riot_api_key_here
export ANTHROPIC_API_KEY=your_anthropic_api_key_here
```

### 2. Build the Project

```bash
./mvnw clean package
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The server will start on http://localhost:8080

### 4. Test the API

In a new terminal window:

```bash
# Using the test script
./test-api.sh

# Or manually with curl
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

## API Endpoints

### Generate Game Report (Query Parameters)
```
GET /api/game-tracker/report?gameName=<NAME>&tagLine=<TAG>
```

**Example:**
```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

### Generate Game Report (Path Parameters)
```
GET /api/game-tracker/report/<NAME>/<TAG>
```

**Example:**
```bash
curl "http://localhost:8080/api/game-tracker/report/Tekindar666/8848"
```

## How It Works

1. **Lookup Player**: Uses Orianna to fetch summoner data by name
2. **Get Latest Match**: Retrieves the most recent game from match history
3. **Analyze Match**: Extracts important statistics:
   - Kills, deaths, assists (KDA)
   - Multi-kills (double, triple, quadra, penta)
   - Gold earned, vision score
   - Champion played, role, game duration
   - Win/loss status
4. **Generate Report**: Sends data to Claude AI for professional analysis
5. **Return Report**: Returns a detailed game report with:
   - Performance summary
   - Key strengths and highlights
   - Areas for improvement
   - Notable moments
   - Recommendations

## Troubleshooting

### Build Fails
- Ensure you have Java 25 installed: `java -version`
- Clean and rebuild: `./mvnw clean compile`

### API Key Errors
- Verify your API keys are set: `echo $RIOT_API_KEY` and `echo $ANTHROPIC_API_KEY`
- Make sure they're exported in the same terminal where you run the app

### Summoner Not Found
- Use the exact in-game summoner name (case-sensitive)
- The tagLine is for display only; lookup uses the gameName

### No Matches Found
- Ensure the player has played at least one game recently
- Check that the player is on the North America server

## Architecture

```
Controller Layer (GameTrackerController)
    ↓
Service Layer (GameTrackerService)
    ↓
    ├── SummonerService → Fetch player by name
    ├── MatchService → Get latest match
    ├── GameAnalysisService → Extract events & stats
    └── ClaudeService → Generate AI report
```

## Dependencies

- Spring Boot 3.5.7
- Orianna 4.0.0-rc8 (Riot API wrapper)
- Anthropic Java SDK 2.11.1 (Claude AI)

## Next Steps

- Add support for match history (multiple games)
- Implement caching to reduce API calls
- Add detailed timeline events (objective timings, team fights)
- Create a web UI for easier access
- Add support for other regions beyond NA
