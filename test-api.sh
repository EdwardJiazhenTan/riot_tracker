#!/bin/bash

# Test script for Riot Game Tracker API

# Check if server is running
echo "Testing if server is running on port 8080..."
if ! curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "Server doesn't seem to be running. Start it with: ./mvnw spring-boot:run"
    echo ""
    echo "Make sure to set these environment variables first:"
    echo "  export RIOT_API_KEY=your_riot_api_key"
    echo "  export ANTHROPIC_API_KEY=your_anthropic_api_key"
    exit 1
fi

echo "Server is running!"
echo ""
echo "Testing game report for Tekindar666#8848..."
echo ""

curl -s "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"

echo ""
echo ""
echo "Done!"
