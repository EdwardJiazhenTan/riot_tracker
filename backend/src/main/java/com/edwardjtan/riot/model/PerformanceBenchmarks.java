package com.edwardjtan.riot.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Hardcoded performance benchmarks for League of Legends by rank and role.
 * Based on community statistics from LeagueMath and LeagueOfLegendsTools.
 */
public class PerformanceBenchmarks {

    public enum Rank {
        IRON, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND, MASTER, GRANDMASTER, CHALLENGER
    }

    public enum Role {
        TOP, JUNGLE, MID, ADC, SUPPORT
    }

    public static class Benchmark {
        public final double csPerMin;
        public final double wardsPerMin;
        public final double damagePerMin;
        public final double goldPerMin;
        public final double kda;

        public Benchmark(double csPerMin, double wardsPerMin, double damagePerMin, double goldPerMin, double kda) {
            this.csPerMin = csPerMin;
            this.wardsPerMin = wardsPerMin;
            this.damagePerMin = damagePerMin;
            this.goldPerMin = goldPerMin;
            this.kda = kda;
        }
    }

    private static final Map<Rank, Map<Role, Benchmark>> BENCHMARKS = new HashMap<>();

    static {
        // Initialize benchmarks for each rank and role
        initializeGoldBenchmarks();
        initializeSilverBenchmarks();
        initializePlatinumBenchmarks();
        // Add more ranks as needed
    }

    private static void initializeGoldBenchmarks() {
        Map<Role, Benchmark> goldBenchmarks = new HashMap<>();

        // Gold rank benchmarks (based on research)
        goldBenchmarks.put(Role.TOP, new Benchmark(
            5.5,    // CS/min
            0.22,   // Wards/min
            580,    // Damage/min
            420,    // Gold/min
            2.6     // KDA
        ));

        goldBenchmarks.put(Role.JUNGLE, new Benchmark(
            1.6,    // CS/min (camps only)
            0.26,   // Wards/min
            550,    // Damage/min
            400,    // Gold/min
            2.8     // KDA
        ));

        goldBenchmarks.put(Role.MID, new Benchmark(
            5.0,    // CS/min
            0.24,   // Wards/min
            680,    // Damage/min
            430,    // Gold/min
            2.7     // KDA
        ));

        goldBenchmarks.put(Role.ADC, new Benchmark(
            5.9,    // CS/min
            0.18,   // Wards/min
            700,    // Damage/min
            440,    // Gold/min
            2.5     // KDA
        ));

        goldBenchmarks.put(Role.SUPPORT, new Benchmark(
            0.9,    // CS/min
            0.69,   // Wards/min
            380,    // Damage/min
            320,    // Gold/min
            3.0     // KDA
        ));

        BENCHMARKS.put(Rank.GOLD, goldBenchmarks);
    }

    private static void initializeSilverBenchmarks() {
        Map<Role, Benchmark> silverBenchmarks = new HashMap<>();

        // Silver rank benchmarks (slightly lower than Gold)
        silverBenchmarks.put(Role.TOP, new Benchmark(5.0, 0.20, 520, 390, 2.4));
        silverBenchmarks.put(Role.JUNGLE, new Benchmark(1.4, 0.24, 500, 370, 2.6));
        silverBenchmarks.put(Role.MID, new Benchmark(4.5, 0.22, 620, 400, 2.5));
        silverBenchmarks.put(Role.ADC, new Benchmark(5.4, 0.16, 640, 410, 2.3));
        silverBenchmarks.put(Role.SUPPORT, new Benchmark(0.8, 0.65, 350, 300, 2.8));

        BENCHMARKS.put(Rank.SILVER, silverBenchmarks);
    }

    private static void initializePlatinumBenchmarks() {
        Map<Role, Benchmark> platinumBenchmarks = new HashMap<>();

        // Platinum rank benchmarks (higher than Gold)
        platinumBenchmarks.put(Role.TOP, new Benchmark(6.0, 0.24, 630, 450, 2.8));
        platinumBenchmarks.put(Role.JUNGLE, new Benchmark(1.8, 0.28, 600, 430, 3.0));
        platinumBenchmarks.put(Role.MID, new Benchmark(5.5, 0.26, 740, 460, 2.9));
        platinumBenchmarks.put(Role.ADC, new Benchmark(6.4, 0.20, 760, 470, 2.7));
        platinumBenchmarks.put(Role.SUPPORT, new Benchmark(1.0, 0.73, 410, 340, 3.2));

        BENCHMARKS.put(Rank.PLATINUM, platinumBenchmarks);
    }

    /**
     * Get benchmark for a specific rank and role
     */
    public static Benchmark getBenchmark(Rank rank, Role role) {
        Map<Role, Benchmark> rankBenchmarks = BENCHMARKS.get(rank);
        if (rankBenchmarks == null) {
            // Default to GOLD if rank not found
            rankBenchmarks = BENCHMARKS.get(Rank.GOLD);
        }

        Benchmark benchmark = rankBenchmarks.get(role);
        if (benchmark == null) {
            // Default to MID if role not found
            benchmark = rankBenchmarks.get(Role.MID);
        }

        return benchmark;
    }

    /**
     * Detect role from position string (Riot API format: teamPosition or individualPosition)
     * Supports: TOP, JUNGLE, MIDDLE, BOTTOM, UTILITY
     */
    public static Role detectRole(String position) {
        if (position == null || position.isEmpty() || position.equals("UNKNOWN")) {
            return Role.MID; // Default
        }

        switch (position.toUpperCase()) {
            case "TOP":
                return Role.TOP;
            case "JUNGLE":
                return Role.JUNGLE;
            case "MIDDLE":
            case "MID":
                return Role.MID;
            case "BOTTOM":
            case "BOT":
                return Role.ADC; // Assume ADC for bottom lane
            case "UTILITY":
            case "SUPPORT":
                return Role.SUPPORT;
            // Legacy lane values
            case "NONE":
            default:
                return Role.MID; // Default fallback
        }
    }

    /**
     * For now, default to GOLD rank. In the future, could detect from match data or summoner API
     */
    public static Rank getDefaultRank() {
        return Rank.GOLD;
    }
}
