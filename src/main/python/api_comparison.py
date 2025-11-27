#!/usr/bin/env python3
"""
CLI tool to compare different AI API providers for speed and token usage.
Called by Spring Boot backend.
"""
import click
import time
import json
import sys
from typing import Dict, Any
from datetime import datetime
import anthropic
import openai
from dotenv import load_dotenv
import os

load_dotenv()


def test_anthropic(prompt: str, model: str = "claude-sonnet-4-20250514") -> Dict[str, Any]:
    """Test Anthropic Claude API."""
    client = anthropic.Anthropic(api_key=os.getenv("ANTHROPIC_API_KEY"))

    start_time = time.time()
    try:
        response = client.messages.create(
            model=model,
            max_tokens=1024,
            messages=[{"role": "user", "content": prompt}]
        )
        end_time = time.time()

        return {
            "provider": "Anthropic",
            "model": model,
            "success": True,
            "responseTime": end_time - start_time,
            "inputTokens": response.usage.input_tokens,
            "outputTokens": response.usage.output_tokens,
            "totalTokens": response.usage.input_tokens + response.usage.output_tokens,
            "response": response.content[0].text,
            "timestamp": datetime.now().isoformat()
        }
    except Exception as e:
        end_time = time.time()
        return {
            "provider": "Anthropic",
            "model": model,
            "success": False,
            "responseTime": end_time - start_time,
            "error": str(e),
            "timestamp": datetime.now().isoformat()
        }


def test_openai(prompt: str, model: str = "gpt-4o") -> Dict[str, Any]:
    """Test OpenAI API."""
    client = openai.OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

    start_time = time.time()
    try:
        response = client.chat.completions.create(
            model=model,
            messages=[{"role": "user", "content": prompt}],
            max_tokens=1024
        )
        end_time = time.time()

        return {
            "provider": "OpenAI",
            "model": model,
            "success": True,
            "responseTime": end_time - start_time,
            "inputTokens": response.usage.prompt_tokens,
            "outputTokens": response.usage.completion_tokens,
            "totalTokens": response.usage.total_tokens,
            "response": response.choices[0].message.content,
            "timestamp": datetime.now().isoformat()
        }
    except Exception as e:
        end_time = time.time()
        return {
            "provider": "OpenAI",
            "model": model,
            "success": False,
            "responseTime": end_time - start_time,
            "error": str(e),
            "timestamp": datetime.now().isoformat()
        }


@click.group()
def cli():
    """CLI tool to compare AI API providers for speed and token usage."""
    pass


@cli.command()
@click.option('--prompt', '-p', required=True, help='Prompt to send to all providers')
@click.option('--anthropic-model', default='claude-sonnet-4-20250514', help='Anthropic model to use')
@click.option('--openai-model', default='gpt-4o', help='OpenAI model to use')
def compare(prompt: str, anthropic_model: str, openai_model: str):
    """Compare multiple API providers with the same prompt and output JSON."""
    results = []

    # Test Anthropic
    if os.getenv("ANTHROPIC_API_KEY"):
        result = test_anthropic(prompt, anthropic_model)
        results.append(result)

    # Test OpenAI
    if os.getenv("OPENAI_API_KEY"):
        result = test_openai(prompt, openai_model)
        results.append(result)

    # Output JSON to stdout for Java to parse
    print(json.dumps(results))


@cli.command()
@click.option('--prompt', '-p', required=True, help='Prompt to send')
@click.option('--model', '-m', default='claude-sonnet-4-20250514', help='Model to use')
def anthropic_test(prompt: str, model: str):
    """Test Anthropic Claude API only."""
    result = test_anthropic(prompt, model)
    print(json.dumps(result))


@cli.command()
@click.option('--prompt', '-p', required=True, help='Prompt to send')
@click.option('--model', '-m', default='gpt-4o', help='Model to use')
def openai_test(prompt: str, model: str):
    """Test OpenAI API only."""
    result = test_openai(prompt, model)
    print(json.dumps(result))


if __name__ == '__main__':
    cli()
