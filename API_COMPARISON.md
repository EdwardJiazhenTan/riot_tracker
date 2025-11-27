# API Comparison Feature

This feature allows you to compare different AI API providers (Anthropic Claude, OpenAI) for speed and token usage through your Spring Boot backend.

## Architecture

```
Spring Boot Backend
    ↓
APIComparisonController (REST API)
    ↓
APIComparisonService (Java)
    ↓
Python CLI (api_comparison.py)
    ↓
AI Provider APIs (Anthropic, OpenAI)
```

## Setup

### 1. Install Python Dependencies

```bash
./setup-python.sh
```

This script will:
- Check for Python 3.8+
- Create a virtual environment in `src/main/python/venv`
- Install required packages (click, anthropic, openai, python-dotenv)

### 2. Configure API Keys

Add your API keys to the `.env` file:

```bash
# Required for Anthropic API
ANTHROPIC_API_KEY=your_anthropic_api_key_here

# Optional for OpenAI comparison
OPENAI_API_KEY=your_openai_api_key_here
```

Get your API keys:
- Anthropic: https://console.anthropic.com/
- OpenAI: https://platform.openai.com/api-keys

### 3. Start the Server

```bash
./mvnw spring-boot:run
```

## API Endpoints

### 1. Compare All Providers

Compare response time and token usage across multiple AI providers.

**Endpoint:** `GET /api/comparison/compare`

**Parameters:**
- `prompt` (required): The prompt to send to all providers
- `anthropicModel` (optional): Anthropic model to use (default: `claude-3-5-sonnet-20241022`)
- `openaiModel` (optional): OpenAI model to use (default: `gpt-4o`)

**Example:**

```bash
curl "http://localhost:8080/api/comparison/compare?prompt=Explain%20quantum%20computing%20in%20one%20sentence"
```

**Response:**

```json
[
  {
    "provider": "Anthropic",
    "model": "claude-3-5-sonnet-20241022",
    "success": true,
    "responseTime": 1.234,
    "inputTokens": 15,
    "outputTokens": 42,
    "totalTokens": 57,
    "response": "Quantum computing harnesses quantum mechanics...",
    "timestamp": "2025-11-25T15:30:00.123"
  },
  {
    "provider": "OpenAI",
    "model": "gpt-4o",
    "success": true,
    "responseTime": 1.456,
    "inputTokens": 16,
    "outputTokens": 38,
    "totalTokens": 54,
    "response": "Quantum computing uses quantum bits...",
    "timestamp": "2025-11-25T15:30:01.456"
  }
]
```

### 2. Test Anthropic Only

Test Anthropic Claude API independently.

**Endpoint:** `GET /api/comparison/anthropic`

**Parameters:**
- `prompt` (required): The prompt to send
- `model` (optional): Model to use (default: `claude-3-5-sonnet-20241022`)

**Example:**

```bash
curl "http://localhost:8080/api/comparison/anthropic?prompt=Hello"
```

### 3. Test OpenAI Only

Test OpenAI API independently.

**Endpoint:** `GET /api/comparison/openai`

**Parameters:**
- `prompt` (required): The prompt to send
- `model` (optional): Model to use (default: `gpt-4o`)

**Example:**

```bash
curl "http://localhost:8080/api/comparison/openai?prompt=Hello"
```

## Response Fields

| Field | Type | Description |
|-------|------|-------------|
| `provider` | String | API provider name (Anthropic, OpenAI) |
| `model` | String | Model used for the request |
| `success` | Boolean | Whether the request succeeded |
| `responseTime` | Number | Response time in seconds |
| `inputTokens` | Integer | Number of input tokens used |
| `outputTokens` | Integer | Number of output tokens generated |
| `totalTokens` | Integer | Total tokens (input + output) |
| `response` | String | The actual response text |
| `error` | String | Error message (if success is false) |
| `timestamp` | String | ISO 8601 timestamp |

## Available Models

### Anthropic Models
- `claude-3-5-sonnet-20241022` (default) - Latest Sonnet
- `claude-3-5-haiku-20241022` - Fast, efficient
- `claude-3-opus-20240229` - Most capable

### OpenAI Models
- `gpt-4o` (default) - Multimodal flagship
- `gpt-4o-mini` - Fast and affordable
- `gpt-4-turbo` - Previous generation

## Use Cases

### 1. Speed Comparison

Compare response times for different providers:

```bash
curl "http://localhost:8080/api/comparison/compare?prompt=Write%20a%20haiku"
```

### 2. Token Usage Analysis

Analyze token efficiency across providers:

```bash
curl "http://localhost:8080/api/comparison/compare?prompt=Summarize%20the%20theory%20of%20relativity"
```

### 3. Quality Comparison

Compare response quality for the same prompt:

```bash
curl "http://localhost:8080/api/comparison/compare?prompt=Explain%20recursion%20to%20a%20beginner"
```

## Frontend Visualization Ideas

You can build a frontend to visualize:

1. **Response Time Charts**: Bar/line charts comparing response times
2. **Token Usage Comparison**: Pie charts or stacked bars for token usage
3. **Cost Calculator**: Estimate costs based on token usage
4. **Side-by-Side Responses**: Display responses from different providers
5. **Historical Trends**: Track performance over time

## Troubleshooting

### Python script fails
- Ensure Python 3.8+ is installed: `python3 --version`
- Check virtual environment is set up: `./setup-python.sh`
- Verify API keys are in `.env` file

### Missing API keys
- The service will skip providers without API keys
- Check logs for "Skipping [Provider]: No API key found"

### Slow responses
- API response times vary based on prompt length and complexity
- Consider using faster models (haiku, gpt-4o-mini) for quick tests

## Python CLI Direct Usage

You can also use the Python CLI directly:

```bash
cd src/main/python
source venv/bin/activate
python api_comparison.py compare --prompt "Hello world"
```

## File Structure

```
src/main/
├── java/com/edwardjtan/riot/
│   ├── controller/
│   │   └── APIComparisonController.java    # REST endpoints
│   ├── service/
│   │   └── APIComparisonService.java       # Python CLI caller
│   └── model/
│       └── APIComparisonResult.java        # Response model
└── python/
    ├── api_comparison.py                    # Python CLI
    ├── requirements.txt                     # Python dependencies
    └── venv/                                # Virtual environment
```
