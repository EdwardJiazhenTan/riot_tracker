#!/bin/bash

# Comprehensive API Endpoint Test Script
# Tests all endpoints for the Riot Game Tracker backend

set -e

BASE_URL="http://localhost:8080"
GAME_NAME="Tekindar666"
TAG_LINE="8848"
PUUID="TCNkQh2ogvRunJhpKj0pEmhdOdZJkuKLiosT0SE8FkmT8cSnQpuoHdXgKvOGlWccTJHQIc3y6TeTWA"
MATCH_ID="NA1_5422503771"

echo "========================================="
echo "Testing All Riot Tracker API Endpoints"
echo "========================================="
echo ""

# Function to test an endpoint
test_endpoint() {
    local name=$1
    local url=$2
    local description=$3

    echo "-----------------------------------"
    echo "Test: $name"
    echo "Description: $description"
    echo "URL: $url"
    echo ""

    response=$(curl -s -w "\nHTTP_CODE:%{http_code}" "$url")
    http_code=$(echo "$response" | grep "HTTP_CODE" | cut -d: -f2)
    body=$(echo "$response" | sed '/HTTP_CODE/d')

    if [ "$http_code" == "200" ]; then
        echo "✓ PASSED (HTTP $http_code)"
        echo "Response preview:"
        echo "$body" | head -c 500
        echo ""
        if [ ${#body} -gt 500 ]; then
            echo "... (truncated)"
        fi
    else
        echo "✗ FAILED (HTTP $http_code)"
        echo "Response:"
        echo "$body"
    fi
    echo ""
}

# Test 1: Get Summoner Info
test_endpoint \
    "GET /api/summoner/{name}/{tag}" \
    "$BASE_URL/api/summoner/$GAME_NAME/$TAG_LINE" \
    "Get basic summoner information by name and tag"

# Test 2: Get Recent Matches List
test_endpoint \
    "GET /api/matches" \
    "$BASE_URL/api/matches?gameName=$GAME_NAME&tagLine=$TAG_LINE&count=5" \
    "Get recent 5 matches for a player"

# Test 3: Get Match Details
test_endpoint \
    "GET /api/match/{matchId}" \
    "$BASE_URL/api/match/$MATCH_ID?puuid=$PUUID" \
    "Get detailed match information including all players"

# Test 4: Generate Game Report
test_endpoint \
    "GET /api/game-tracker/report-by-match" \
    "$BASE_URL/api/game-tracker/report-by-match?matchId=$MATCH_ID&puuid=$PUUID" \
    "Generate AI report for a specific match"

# Test 5: Compare AI Providers
test_endpoint \
    "GET /api/game-tracker/compare-by-match" \
    "$BASE_URL/api/game-tracker/compare-by-match?matchId=$MATCH_ID" \
    "Compare Anthropic and OpenAI reports for a match"

# Test 6: Get Game Report (Legacy - by player)
test_endpoint \
    "GET /api/game-tracker/report" \
    "$BASE_URL/api/game-tracker/report?gameName=$GAME_NAME&tagLine=$TAG_LINE" \
    "Generate report for player's latest match"

echo "========================================="
echo "All Tests Complete!"
echo "========================================="
