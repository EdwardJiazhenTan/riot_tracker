package com.edwardjtan.riot.service;

import com.edwardjtan.riot.model.APIComparisonResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class APIComparisonService {

    private static final String PYTHON_SCRIPT = "src/main/python/api_comparison.py";
    private static final String PYTHON_VENV = "src/main/python/venv/bin/python3";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Compare multiple API providers with the same prompt
     */
    public List<APIComparisonResult> compareProviders(String prompt, String anthropicModel, String openaiModel) {
        try {
            List<String> command = new ArrayList<>();
            // Use virtual environment Python if it exists, otherwise fall back to system python3
            String pythonExecutable = new java.io.File(PYTHON_VENV).exists() ? PYTHON_VENV : "python3";
            command.add(pythonExecutable);
            command.add(PYTHON_SCRIPT);
            command.add("compare");
            command.add("--prompt");
            command.add(prompt);

            if (anthropicModel != null && !anthropicModel.isEmpty()) {
                command.add("--anthropic-model");
                command.add(anthropicModel);
            }

            if (openaiModel != null && !openaiModel.isEmpty()) {
                command.add("--openai-model");
                command.add(openaiModel);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code: " + exitCode +
                                         "\nOutput: " + output.toString());
            }

            // Parse JSON output
            return objectMapper.readValue(output.toString(),
                                        new TypeReference<List<APIComparisonResult>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute API comparison: " + e.getMessage(), e);
        }
    }

    /**
     * Test Anthropic API only
     */
    public APIComparisonResult testAnthropic(String prompt, String model) {
        try {
            List<String> command = new ArrayList<>();
            // Use virtual environment Python if it exists, otherwise fall back to system python3
            String pythonExecutable = new java.io.File(PYTHON_VENV).exists() ? PYTHON_VENV : "python3";
            command.add(pythonExecutable);
            command.add(PYTHON_SCRIPT);
            command.add("anthropic-test");
            command.add("--prompt");
            command.add(prompt);

            if (model != null && !model.isEmpty()) {
                command.add("--model");
                command.add(model);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code: " + exitCode +
                                         "\nOutput: " + output.toString());
            }

            return objectMapper.readValue(output.toString(), APIComparisonResult.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to test Anthropic API: " + e.getMessage(), e);
        }
    }

    /**
     * Test OpenAI API only
     */
    public APIComparisonResult testOpenAI(String prompt, String model) {
        try {
            List<String> command = new ArrayList<>();
            // Use virtual environment Python if it exists, otherwise fall back to system python3
            String pythonExecutable = new java.io.File(PYTHON_VENV).exists() ? PYTHON_VENV : "python3";
            command.add(pythonExecutable);
            command.add(PYTHON_SCRIPT);
            command.add("openai-test");
            command.add("--prompt");
            command.add(prompt);

            if (model != null && !model.isEmpty()) {
                command.add("--model");
                command.add(model);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code: " + exitCode +
                                         "\nOutput: " + output.toString());
            }

            return objectMapper.readValue(output.toString(), APIComparisonResult.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to test OpenAI API: " + e.getMessage(), e);
        }
    }
}
