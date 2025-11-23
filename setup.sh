#!/bin/bash

echo "====================================="
echo "Riot Game Tracker Setup"
echo "====================================="
echo ""

# Check if .env file exists
if [ -f .env ]; then
    echo "✓ .env file already exists"
else
    echo "Creating .env file from example..."
    cp .env.example .env
    echo "✓ Created .env file"
    echo ""
    echo "⚠️  IMPORTANT: Edit .env and add your API keys:"
    echo "   - RIOT_API_KEY (from https://developer.riotgames.com/)"
    echo "   - ANTHROPIC_API_KEY (from https://console.anthropic.com/)"
    echo ""
    echo "You can edit it with: nano .env"
    echo ""
fi

# Check if API keys are set in .env
if [ -f .env ]; then
    source .env
    if [ -z "$RIOT_API_KEY" ] || [ "$RIOT_API_KEY" = "your_riot_api_key_here" ]; then
        echo "⚠️  RIOT_API_KEY not set in .env file"
    else
        echo "✓ RIOT_API_KEY is set"
    fi

    if [ -z "$ANTHROPIC_API_KEY" ] || [ "$ANTHROPIC_API_KEY" = "your_anthropic_api_key_here" ]; then
        echo "⚠️  ANTHROPIC_API_KEY not set in .env file"
    else
        echo "✓ ANTHROPIC_API_KEY is set"
    fi
fi

echo ""
echo "Building project..."
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "====================================="
    echo "✓ Setup complete!"
    echo "====================================="
    echo ""
    echo "To start the server, run:"
    echo "  ./mvnw spring-boot:run"
    echo ""
    echo "To test the API, run (in another terminal):"
    echo "  ./test-api.sh"
    echo ""
else
    echo ""
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi
