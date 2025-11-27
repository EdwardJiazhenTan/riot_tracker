# API Documentation

Base URL: `http://localhost:8080`

## Endpoints

### Get Game Report

Generate an AI-powered analysis report for a player's latest League of Legends match.

#### Request

**Query Parameters:**
```http
GET /api/game-tracker/report?gameName={name}&tagLine={tag}
```

**Path Parameters:**
```http
GET /api/game-tracker/report/{gameName}/{tagLine}
```

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| gameName | string | Yes | Player's Riot ID name |
| tagLine | string | Yes | Player's Riot ID tag |

#### Response

**Success (200 OK)**

Returns a markdown-formatted game analysis report.

```
Content-Type: text/plain

# League of Legends Match Analysis Report

**Match ID:** NA1_5417153894
**Player:** Tekindar666#8848
**Champion:** Galio | **Result:** Victory
...
```

**Error (500 Internal Server Error)**

```json
{
  "timestamp": "2025-11-23T20:32:06.168+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed to generate game report: ...",
  "path": "/api/game-tracker/report"
}
```

#### Examples

**cURL:**
```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

```bash
curl "http://localhost:8080/api/game-tracker/report/Tekindar666/8848"
```

**JavaScript (Fetch):**
```javascript
const response = await fetch(
  'http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848'
);
const report = await response.text();
console.log(report);
```

**Python (requests):**
```python
import requests

response = requests.get(
    'http://localhost:8080/api/game-tracker/report',
    params={'gameName': 'Tekindar666', 'tagLine': '8848'}
)
print(response.text)
```

## Response Details

The API returns a comprehensive match analysis including:

- **Match Overview**: Match ID, player info, champion, result, duration, KDA
- **Performance Summary**: Overall grade and performance assessment
- **Key Strengths**: Highlights of excellent plays and decisions
- **Areas for Improvement**: Constructive feedback on weaknesses
- **Notable Moments**: Significant plays and achievements
- **Recommendations**: Actionable advice for future games

## Rate Limits

- Riot API: Varies by API key type (development keys: 20 requests/second, 100 requests/2 minutes)
- Claude API: Varies by account tier

## Notes

- Currently supports **North America (NA)** region only
- Fetches the **most recent match** for the player
- Analysis powered by **Claude Sonnet 4.5**
- Match data includes: kills, deaths, assists, gold earned, vision score, CS, and multi-kills

## Error Codes

| Status | Description |
|--------|-------------|
| 200 | Success - Report generated |
| 500 | Server error - Failed to fetch data or generate report |

Common error causes:
- Invalid summoner name/tag
- Player has no recent matches
- API key issues (expired or invalid)
- Claude API unavailable
