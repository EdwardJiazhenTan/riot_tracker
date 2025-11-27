#!/bin/bash

echo "Setting up Python dependencies for API comparison..."

# Check if python3 is installed
if ! command -v python3 &> /dev/null; then
    echo "Error: python3 is not installed. Please install Python 3.8 or higher."
    exit 1
fi

# Check Python version
PYTHON_VERSION=$(python3 --version | cut -d' ' -f2 | cut -d'.' -f1,2)
echo "Found Python version: $PYTHON_VERSION"

# Install pip if not available
if ! command -v pip3 &> /dev/null; then
    echo "Installing pip..."
    python3 -m ensurepip --upgrade
fi

# Install/upgrade virtualenv
echo "Installing virtualenv..."
pip3 install --upgrade virtualenv

# Create virtual environment in src/main/python
cd src/main/python
if [ ! -d "venv" ]; then
    echo "Creating virtual environment..."
    python3 -m venv venv
else
    echo "Virtual environment already exists."
fi

# Activate virtual environment and install dependencies
echo "Installing Python packages..."
source venv/bin/activate
pip install --upgrade pip
pip install -r requirements.txt

echo ""
echo "✅ Python setup complete!"
echo ""
echo "To use the API comparison feature:"
echo "1. Add your OPENAI_API_KEY to the .env file (optional)"
echo "2. Make sure your ANTHROPIC_API_KEY is in the .env file"
echo "3. Run the Spring Boot application: ./mvnw spring-boot:run"
echo "4. Test the API comparison endpoints:"
echo "   - Compare all providers: curl 'http://localhost:8080/api/comparison/compare?prompt=Hello'"
echo "   - Test Anthropic only: curl 'http://localhost:8080/api/comparison/anthropic?prompt=Hello'"
echo "   - Test OpenAI only: curl 'http://localhost:8080/api/comparison/openai?prompt=Hello'"
echo ""
echo "Python virtual environment is located at: src/main/python/venv"
echo "To manually activate it: source src/main/python/venv/bin/activate"
