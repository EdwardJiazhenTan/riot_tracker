# Quick Start - 3 Steps

## Step 1: Setup
```bash
./setup.sh
```

This will:
- Create a `.env` file from the example
- Build the project

## Step 2: Add Your API Keys

Edit the `.env` file:
```bash
nano .env
```

Replace the placeholder values:
```
RIOT_API_KEY=your_actual_riot_key
ANTHROPIC_API_KEY=your_actual_anthropic_key
```

**Get API Keys:**
- Riot: https://developer.riotgames.com/
- Anthropic: https://console.anthropic.com/

## Step 3: Run

Start the server:
```bash
./mvnw spring-boot:run
```

In a new terminal, test it:
```bash
./test-api.sh
```

Or manually:
```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

## That's it!

You'll get a detailed AI-powered analysis of the latest game.

## Troubleshooting

**"Summoner not found"**
- Use the exact in-game name (case-sensitive)
- Make sure the player is on NA server

**"No matches found"**
- Player needs at least one recent game

**Build fails**
- Check Java version: `java -version` (needs Java 25)
- Clean build: `./mvnw clean compile`

**API errors**
- Verify keys are set: `cat .env`
- Check keys are valid on the respective platforms
