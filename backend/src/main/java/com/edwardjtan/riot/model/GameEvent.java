package com.edwardjtan.riot.model;

public class GameEvent {
  private final String type;
  private final long timestamp;
  private final String description;
  private final String participantName;

  public GameEvent(String type, long timestamp, String description, String participantName) {
    this.type = type;
    this.timestamp = timestamp;
    this.description = description;
    this.participantName = participantName;
  }

  public String getType() {
    return type;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getDescription() {
    return description;
  }

  public String getParticipantName() {
    return participantName;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s - %s (by %s)",
      formatTimestamp(timestamp), type, description, participantName);
  }

  private String formatTimestamp(long millis) {
    long seconds = millis / 1000;
    long minutes = seconds / 60;
    seconds = seconds % 60;
    return String.format("%02d:%02d", minutes, seconds);
  }
}
