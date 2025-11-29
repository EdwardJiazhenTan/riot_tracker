# Frontend API Guide

Complete guide for frontend developers to integrate with the Riot Game Tracker API.

## Base URL
```
http://localhost:8080
```

---

## Table of Contents
1. [Get Summoner Info](#1-get-summoner-info)
2. [Get Recent Matches](#2-get-recent-matches)
3. [Get Match Details](#3-get-match-details)
4. [Generate AI Report](#4-generate-ai-report)
5. [Compare AI Providers](#5-compare-ai-providers)
6. [TypeScript Interfaces](#typescript-interfaces)
7. [Complete Examples](#complete-examples)

---

## 1. Get Summoner Info

Get basic summoner information (level, PUUID).

### Endpoint
```
GET /api/summoner/{gameName}/{tagLine}
```

### Parameters
- `gameName`: Player's game name (e.g., "Tekindar666")
- `tagLine`: Player's tag (e.g., "8848")

### Example Request
```javascript
const response = await fetch(
  'http://localhost:8080/api/summoner/Tekindar666/8848'
);
const data = await response.text();
```

### Example Response (Plain Text)
```
Summoner: null, Level: 417, ID: null, PUUID: TCNkQh2ogvRunJhpKj0pEmhdOdZJkuKLiosT0SE8FkmT8cSnQpuoHdXgKvOGlWccTJHQIc3y6TeTWA
```

### Use Case
- Validate summoner exists
- Get PUUID for other API calls
- Display summoner level

---

## 2. Get Recent Matches

Get a list of recent matches for a player.

### Endpoint
```
GET /api/matches?gameName={name}&tagLine={tag}&count={count}
```

### Parameters
- `gameName` (required): Player's game name
- `tagLine` (required): Player's tag
- `count` (optional): Number of matches to return (default: 20)

### Example Request
```javascript
const response = await fetch(
  'http://localhost:8080/api/matches?gameName=Tekindar666&tagLine=8848&count=10'
);
const matches = await response.json();
```

### Example Response (JSON)
```json
[
  {
    "matchId": "NA1_5422503771",
    "championName": "Jayce",
    "kills": 2,
    "deaths": 5,
    "assists": 3,
    "win": false,
    "gameDuration": 1096,
    "gameCreation": 1764216374438
  },
  {
    "matchId": "NA1_5422473814",
    "championName": "Galio",
    "kills": 1,
    "deaths": 3,
    "assists": 11,
    "win": true,
    "gameDuration": 1540,
    "gameCreation": 1764214489551
  }
]
```

### Response Fields
| Field | Type | Description |
|-------|------|-------------|
| `matchId` | string | Unique match identifier |
| `championName` | string | Champion played |
| `kills` | number | Number of kills |
| `deaths` | number | Number of deaths |
| `assists` | number | Number of assists |
| `win` | boolean | Match result (true = victory) |
| `gameDuration` | number | Game duration in seconds |
| `gameCreation` | number | Timestamp in milliseconds |

### Use Case
- Display match history list
- Show recent performance
- Navigate to match details

---

## 3. Get Match Details

Get detailed information about a specific match, including all players.

### Endpoint
```
GET /api/match/{matchId}?puuid={puuid}
```

### Parameters
- `matchId` (required): Match ID from the matches list
- `puuid` (required): Player's PUUID

### Example Request
```javascript
const matchId = 'NA1_5422503771';
const puuid = 'TCNkQh2ogvRunJhpKj0pEmhdOdZJkuKLiosT0SE8FkmT8cSnQpuoHdXgKvOGlWccTJHQIc3y6TeTWA';

const response = await fetch(
  `http://localhost:8080/api/match/${matchId}?puuid=${puuid}`
);
const details = await response.json();
```

### Example Response (JSON)
```json
{
  "matchId": "NA1_5422503771",
  "gameDuration": 1096,
  "gameCreation": 1764216374438,
  "playerStats": {
    "puuid": "TCNkQh2ogvRunJhpKj0pEmhdOdZJkuKLiosT0SE8FkmT8cSnQpuoHdXgKvOGlWccTJHQIc3y6TeTWA",
    "riotIdGameName": "Tekindar666",
    "riotIdTagline": "8848",
    "championName": "Jayce",
    "kills": 2,
    "deaths": 5,
    "assists": 3,
    "win": false,
    "goldEarned": 6282,
    "visionScore": 12,
    "totalMinionsKilled": 108,
    "neutralMinionsKilled": 0,
    "doubleKills": 0,
    "tripleKills": 0,
    "quadraKills": 0,
    "pentaKills": 0,
    "lane": "NONE",
    "summoner1Id": 12,
    "summoner2Id": 4,
    "championAvatarUrl": "https://ddragon.leagueoflegends.com/cdn/14.23.1/img/champion/Jayce.png"
  },
  "allPlayers": [
    {
      "championName": "Sett",
      "kills": 11,
      "deaths": 2,
      "assists": 5,
      "win": true,
      "championAvatarUrl": "https://ddragon.leagueoflegends.com/cdn/14.23.1/img/champion/Sett.png",
      "summoner1Id": 4,
      "summoner2Id": 14
    }
    // ... 9 more players
  ]
}
```

### Response Fields - PlayerStats
| Field | Type | Description |
|-------|------|-------------|
| `championName` | string | Champion name |
| `championAvatarUrl` | string | Full URL to champion image |
| `kills/deaths/assists` | number | KDA stats |
| `win` | boolean | Match result |
| `goldEarned` | number | Total gold earned |
| `visionScore` | number | Vision score |
| `totalMinionsKilled` | number | CS (minions killed) |
| `neutralMinionsKilled` | number | Jungle CS |
| `doubleKills` | number | Double kills |
| `tripleKills` | number | Triple kills |
| `quadraKills` | number | Quadra kills |
| `pentaKills` | number | Penta kills |
| `summoner1Id` | number | First summoner spell ID |
| `summoner2Id` | number | Second summoner spell ID |
| `lane` | string | Lane position |

### Use Case
- Display detailed match page
- Show all players' performance
- Compare stats across teams

---

## 4. Generate AI Report

Generate an AI-powered analysis report for a specific match.

### Endpoint
```
GET /api/game-tracker/report-by-match?matchId={matchId}&puuid={puuid}
```

### Parameters
- `matchId` (required): Match ID
- `puuid` (required): Player's PUUID

### Example Request
```javascript
const response = await fetch(
  'http://localhost:8080/api/game-tracker/report-by-match?matchId=NA1_5422503771&puuid=TCNkQh2ogvRunJhpKj0pEmhdOdZJkuKLiosT0SE8FkmT8cSnQpuoHdXgKvOGlWccTJHQIc3y6TeTWA'
);
const report = await response.text();
```

### Example Response (Markdown Text)
```markdown
# League of Legends Match Analysis Report

**Match ID:** NA1_5422503771
**Player:** Tekindar666#8848
**Champion:** Jayce | **Role:** NONE | **Result:** DEFEAT
**Duration:** 18:16 | **KDA:** 2/5/3 (1.0 KDA Ratio)

---

## 1. Overall Performance Summary

This was a challenging early game that ended in defeat...

## 2. Key Strengths and Highlights

- Good CS management with 108 minions in 18 minutes
- Maintained vision control with score of 12

## 3. Areas for Improvement

- Work on positioning to reduce deaths
- Focus on early game impact
...
```

### Use Case
- Display AI analysis for player improvement
- Show match insights
- Provide coaching feedback

---

## 5. Compare AI Providers

Compare reports from different AI providers (Anthropic vs OpenAI) with speed and token metrics.

### Endpoint
```
GET /api/game-tracker/compare-by-match?matchId={matchId}&anthropicModel={model}&openaiModel={model}
```

### Parameters
- `matchId` (required): Match ID
- `anthropicModel` (optional): Anthropic model name (default: "claude-sonnet-4-20250514")
- `openaiModel` (optional): OpenAI model name (default: "gpt-4o")

### Example Request
```javascript
const response = await fetch(
  'http://localhost:8080/api/game-tracker/compare-by-match?matchId=NA1_5422503771'
);
const comparisons = await response.json();
```

### Example Response (JSON Array)
```json
[
  {
    "provider": "Anthropic",
    "model": "claude-sonnet-4-20250514",
    "success": true,
    "report": "# League of Legends Match Report\n\n**Match ID:** NA1_5422503771...",
    "error": null,
    "timestamp": "2025-11-28T11:11:47.000137",
    "matchId": "NA1_5422503771",
    "responseTime": 17.174,
    "inputTokens": 284,
    "outputTokens": 627,
    "totalTokens": 911
  },
  {
    "provider": "OpenAI",
    "model": "gpt-4o",
    "success": true,
    "report": "**Match Report: NA1_5422503771**\n\n...",
    "responseTime": 17.800,
    "inputTokens": 240,
    "outputTokens": 592,
    "totalTokens": 832
  }
]
```

### Response Fields - GameReportComparison
| Field | Type | Description |
|-------|------|-------------|
| `provider` | string | "Anthropic" or "OpenAI" |
| `model` | string | Model used for generation |
| `success` | boolean | Whether generation succeeded |
| `report` | string | AI-generated markdown report |
| `error` | string\|null | Error message if failed |
| `responseTime` | number | Generation time in seconds |
| `inputTokens` | number | Input tokens used |
| `outputTokens` | number | Output tokens generated |
| `totalTokens` | number | Total tokens (input + output) |

### Use Case
- Compare AI provider performance
- Show speed/cost differences
- Advanced analytics view

---

## TypeScript Interfaces

### MatchSummary
```typescript
interface MatchSummary {
  matchId: string;
  championName: string;
  kills: number;
  deaths: number;
  assists: number;
  win: boolean;
  gameDuration: number;      // in seconds
  gameCreation: number;      // timestamp in milliseconds
}
```

### MatchDetails
```typescript
interface MatchDetails {
  matchId: string;
  gameDuration: number;
  gameCreation: number;
  playerStats: PlayerStats;
  allPlayers: PlayerStats[];
}

interface PlayerStats {
  puuid: string;
  summonerName: string;
  riotIdGameName: string;
  riotIdTagline: string;
  championName: string;
  championAvatarUrl: string;
  kills: number;
  deaths: number;
  assists: number;
  win: boolean;
  goldEarned: number;
  visionScore: number;
  totalMinionsKilled: number;
  neutralMinionsKilled: number;
  doubleKills: number;
  tripleKills: number;
  quadraKills: number;
  pentaKills: number;
  lane: string;
  summoner1Id: number;
  summoner2Id: number;
}
```

### GameReportComparison
```typescript
interface GameReportComparison {
  provider: string;
  model: string;
  success: boolean;
  report: string;
  error: string | null;
  timestamp: string;
  matchId: string;
  responseTime: number;
  inputTokens: number;
  outputTokens: number;
  totalTokens: number;
}
```

---

## Complete Examples

### Example 1: Display Match History

```typescript
import React, { useState, useEffect } from 'react';

interface MatchHistoryProps {
  gameName: string;
  tagLine: string;
}

export function MatchHistory({ gameName, tagLine }: MatchHistoryProps) {
  const [matches, setMatches] = useState<MatchSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchMatches() {
      const response = await fetch(
        `http://localhost:8080/api/matches?gameName=${gameName}&tagLine=${tagLine}&count=10`
      );
      const data = await response.json();
      setMatches(data);
      setLoading(false);
    }
    fetchMatches();
  }, [gameName, tagLine]);

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      {matches.map((match) => (
        <div key={match.matchId}
             style={{ backgroundColor: match.win ? '#d4edda' : '#f8d7da' }}>
          <h3>{match.championName} - {match.win ? 'Victory' : 'Defeat'}</h3>
          <p>KDA: {match.kills}/{match.deaths}/{match.assists}</p>
          <p>Duration: {Math.floor(match.gameDuration / 60)}m</p>
        </div>
      ))}
    </div>
  );
}
```

### Example 2: Display Match Details with Avatars

```typescript
export function MatchDetailsView({ matchId, puuid }: { matchId: string; puuid: string }) {
  const [details, setDetails] = useState<MatchDetails | null>(null);

  useEffect(() => {
    async function fetchDetails() {
      const response = await fetch(
        `http://localhost:8080/api/match/${matchId}?puuid=${puuid}`
      );
      const data = await response.json();
      setDetails(data);
    }
    fetchDetails();
  }, [matchId, puuid]);

  if (!details) return <div>Loading...</div>;

  return (
    <div>
      <h2>Your Performance</h2>
      <div>
        <img
          src={details.playerStats.championAvatarUrl}
          alt={details.playerStats.championName}
          width={48}
          height={48}
        />
        <h3>{details.playerStats.championName}</h3>
        <p>KDA: {details.playerStats.kills}/{details.playerStats.deaths}/{details.playerStats.assists}</p>
      </div>

      <h2>All Players</h2>
      {details.allPlayers.map((player) => (
        <div key={player.puuid}>
          <img src={player.championAvatarUrl} width={32} height={32} />
          <span>{player.riotIdGameName}#{player.riotIdTagline}</span>
          <span>{player.kills}/{player.deaths}/{player.assists}</span>
        </div>
      ))}
    </div>
  );
}
```

### Example 3: AI Report Comparison

```typescript
export function AIComparison({ matchId }: { matchId: string }) {
  const [comparisons, setComparisons] = useState<GameReportComparison[]>([]);

  useEffect(() => {
    async function fetchComparison() {
      const response = await fetch(
        `http://localhost:8080/api/game-tracker/compare-by-match?matchId=${matchId}`
      );
      const data = await response.json();
      setComparisons(data);
    }
    fetchComparison();
  }, [matchId]);

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
      {comparisons.map((result) => (
        <div key={result.provider} style={{ border: '1px solid #ccc', padding: '16px' }}>
          <h2>{result.provider} - {result.model}</h2>
          <p><strong>Speed:</strong> {result.responseTime.toFixed(2)}s</p>
          <p><strong>Tokens:</strong> {result.totalTokens} (in: {result.inputTokens}, out: {result.outputTokens})</p>
          <div style={{ whiteSpace: 'pre-wrap', marginTop: '16px' }}>
            {result.report}
          </div>
        </div>
      ))}
    </div>
  );
}
```

---

## Helper Functions

### Calculate KDA Ratio
```typescript
function calculateKDA(kills: number, deaths: number, assists: number): string {
  if (deaths === 0) return 'Perfect';
  return ((kills + assists) / deaths).toFixed(2);
}
```

### Format Duration
```typescript
function formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}m ${secs}s`;
}
```

### Get Summoner Spell Image
```typescript
const SUMMONER_SPELLS: Record<number, string> = {
  1: 'SummonerBoost',      // Cleanse
  3: 'SummonerExhaust',    // Exhaust
  4: 'SummonerFlash',      // Flash
  6: 'SummonerHaste',      // Ghost
  7: 'SummonerHeal',       // Heal
  11: 'SummonerSmite',     // Smite
  12: 'SummonerTeleport',  // Teleport
  14: 'SummonerDot',       // Ignite
  21: 'SummonerBarrier',   // Barrier
};

function getSummonerSpellUrl(spellId: number): string {
  const DD_VERSION = '14.23.1';
  const spellName = SUMMONER_SPELLS[spellId] || 'SummonerFlash';
  return `https://ddragon.leagueoflegends.com/cdn/${DD_VERSION}/img/spell/${spellName}.png`;
}
```

---

## Error Handling

All endpoints return appropriate HTTP status codes:
- `200 OK`: Success
- `500 Internal Server Error`: Server error (check error message in response)

Example error handling:
```typescript
async function fetchMatches(gameName: string, tagLine: string) {
  try {
    const response = await fetch(
      `http://localhost:8080/api/matches?gameName=${gameName}&tagLine=${tagLine}`
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error fetching matches:', error);
    throw error;
  }
}
```

---

## Notes

1. **CORS**: All endpoints have CORS enabled for development (`origins = "*"`)
2. **Champion Avatar URLs**: Pre-generated URLs use Data Dragon version `14.23.1`
3. **Summoner Spells**: IDs from Riot API - use mapping table to get spell names
4. **Time Values**:
   - `gameDuration`: seconds
   - `gameCreation`: milliseconds (use `new Date(gameCreation)`)
5. **PUUID**: Required for most endpoints - get from `/api/summoner` first

---

## Testing

Test all endpoints using the provided test script:
```bash
cd /Users/etan/Projects/riot/backend
./test-all-endpoints.sh
```
