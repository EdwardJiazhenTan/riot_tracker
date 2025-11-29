# Game Report Comparison Feature

Compare different AI providers (Anthropic Claude vs OpenAI) when generating League of Legends game analysis reports.

## New Endpoint

### Compare Game Reports

**Endpoint:** `GET /api/game-tracker/compare`

**Description:** Analyzes your latest League of Legends match and generates reports using multiple AI providers, allowing you to compare:
- Response speed
- Token usage (cost)
- Report quality and style
- Different AI perspectives on the same game

**Parameters:**
- `gameName` (required): Your Riot game name (e.g., "Tekindar666")
- `tagLine` (required): Your tagline (e.g., "8848")
- `anthropicModel` (optional): Anthropic model to use (default: `claude-sonnet-4-20250514`)
- `openaiModel` (optional): OpenAI model to use (default: `gpt-4o`)

**Example:**

```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=Tekindar666&tagLine=8848"
```

**Response:**

```json
[
  {
    "provider": "Anthropic",
    "model": "claude-sonnet-4-20250514",
    "success": true,
    "report": "# League of Legends Match Analysis Report\n\n**Match ID:** NA1_5421125933...",
    "responseTime": 2.45,
    "inputTokens": 450,
    "outputTokens": 520,
    "totalTokens": 970,
    "timestamp": "2025-11-25T23:00:00.000",
    "gameName": "Tekindar666",
    "tagLine": "8848",
    "matchId": "NA1_5421125933"
  },
  {
    "provider": "OpenAI",
    "model": "gpt-4o",
    "success": true,
    "report": "Match Analysis for Tekindar666#8848...",
    "responseTime": 1.82,
    "inputTokens": 445,
    "outputTokens": 498,
    "totalTokens": 943,
    "timestamp": "2025-11-25T23:00:02.450",
    "gameName": "Tekindar666",
    "tagLine": "8848",
    "matchId": "NA1_5421125933"
  }
]
```

## Use Cases

### 1. Speed Comparison
Compare which AI provider generates reports faster:

```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=YourName&tagLine=YourTag"
```

Look at the `responseTime` field to see which is faster.

### 2. Cost Analysis
Compare token usage to estimate costs:

```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=YourName&tagLine=YourTag"
```

- Anthropic Claude pricing: ~$3 per million input tokens, ~$15 per million output tokens
- OpenAI GPT-4o pricing: ~$2.50 per million input tokens, ~$10 per million output tokens

### 3. Quality Comparison
Compare report quality and writing style:

```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=YourName&tagLine=YourTag"
```

Read the `report` field from each provider to see which analysis style you prefer.

### 4. Model Comparison
Test different models from the same provider:

```bash
# Compare Claude Sonnet vs Haiku (faster, cheaper)
curl "http://localhost:8080/api/game-tracker/compare?gameName=YourName&tagLine=YourTag&anthropicModel=claude-3-5-haiku-20241022"

# Compare GPT-4o vs GPT-4o-mini
curl "http://localhost:8080/api/game-tracker/compare?gameName=YourName&tagLine=YourTag&openaiModel=gpt-4o-mini"
```

## Available Models

### Anthropic Models
- `claude-sonnet-4-20250514` (default) - Best quality, balanced speed
- `claude-3-5-haiku-20241022` - Fastest, most affordable
- `claude-3-opus-20240229` - Most capable, slowest

### OpenAI Models
- `gpt-4o` (default) - Fast and capable
- `gpt-4o-mini` - Faster and cheaper
- `gpt-4-turbo` - Previous generation

## Integration with Existing Endpoints

### Original Report Endpoint (unchanged)
Still works as before, uses only Anthropic Claude:

```bash
curl "http://localhost:8080/api/game-tracker/report?gameName=Tekindar666&tagLine=8848"
```

### New Comparison Endpoint
Compare multiple providers:

```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=Tekindar666&tagLine=8848"
```

## Frontend Visualization Ideas

You can build a frontend that displays:

1. **Side-by-Side Reports**: Show both AI reports next to each other
2. **Performance Metrics**:
   - Bar chart comparing response times
   - Pie chart showing token usage breakdown
3. **Cost Calculator**:
   - Show estimated cost per report
   - Monthly cost projections based on usage
4. **Quality Ratings**:
   - Let users rate which report they prefer
   - Track which AI performs better over time
5. **Model Selector**:
   - Dropdown to choose models
   - Save preferred model per user

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| `provider` | String | AI provider name (Anthropic, OpenAI) |
| `model` | String | Model used |
| `success` | Boolean | Whether report generation succeeded |
| `report` | String | The generated game analysis report (markdown) |
| `error` | String | Error message if failed |
| `responseTime` | Number | Time taken in seconds |
| `inputTokens` | Integer | Input tokens used |
| `outputTokens` | Integer | Output tokens generated |
| `totalTokens` | Integer | Total tokens (input + output) |
| `timestamp` | String | When the report was generated |
| `gameName` | String | Player's game name |
| `tagLine` | String | Player's tag line |
| `matchId` | String | League of Legends match ID |

## Example Workflow

1. **Get your latest game analyzed by multiple AIs:**
```bash
curl "http://localhost:8080/api/game-tracker/compare?gameName=Tekindar666&tagLine=8848"
```

2. **Save the response** to see side-by-side comparisons

3. **Choose your preferred provider** based on:
   - Report quality
   - Response speed
   - Token usage/cost

4. **Use the standard endpoint** with your preferred provider for daily use

## Cost Estimation Example

If a typical game report uses:
- Input: ~450 tokens
- Output: ~520 tokens

**Per report cost:**
- Anthropic Claude Sonnet: ~$0.009 (0.9 cents)
- OpenAI GPT-4o: ~$0.006 (0.6 cents)

**100 reports per month:**
- Anthropic: ~$0.90
- OpenAI: ~$0.60

Both are very affordable! Choose based on quality preference.
