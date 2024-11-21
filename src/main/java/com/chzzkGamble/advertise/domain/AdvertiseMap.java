package com.chzzkGamble.advertise.domain;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AdvertiseMap {

    private static final Advertise DEFAULT_ADVERTISE = new Advertise("Default Advertise", "IMAGE_URL_HERE", 0L);
    private static final long ADVERTISE_DURATION_DAYS = 10L;

    private final Map<Advertise, Long> adCumulativeCosts;
    private final Map<Advertise, Double> adProbabilities;
    private final long totalAmount;

    private AdvertiseMap(Map<Advertise, Long> adCumulativeCosts, Map<Advertise, Double> adProbabilities, long totalAmount) {
        this.adCumulativeCosts = adCumulativeCosts;
        this.adProbabilities = adProbabilities;
        this.totalAmount = totalAmount;
    }

    public static AdvertiseMap from(List<Advertise> advertises, Clock clock) {
        long totalAmount = 0L;
        Map<Advertise, Long> costMap = new LinkedHashMap<>();
        Map<Advertise, Double> probabilityMap = new HashMap<>();

        for (Advertise advertise : advertises) {
            totalAmount += getAdjustedCost(advertise, clock);
            costMap.put(advertise, totalAmount);
        }
        for (Advertise advertise : advertises) {
            probabilityMap.put(advertise, getAdjustedCost(advertise, clock) / (double) totalAmount);
        }

        return new AdvertiseMap(costMap, probabilityMap, totalAmount);
    }

    private static Long getAdjustedCost(Advertise advertise, Clock clock) {
        Duration duration = Duration.between(advertise.getStartDate(), LocalDateTime.now(clock));
        long pastDays = duration.getSeconds() / 86400;
        return advertise.getCost() * (ADVERTISE_DURATION_DAYS - pastDays) / ADVERTISE_DURATION_DAYS;
    }

    public Advertise getRandom() {
        if (totalAmount == 0) return DEFAULT_ADVERTISE;

        long threshold = ThreadLocalRandom.current().nextLong(totalAmount);
        return adCumulativeCosts.entrySet().stream()
                .filter(entry -> threshold <= entry.getValue())
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(DEFAULT_ADVERTISE);
    }

    public Map<Advertise, Double> getProbabilities() {
        return Map.copyOf(adProbabilities);
    }

    @Override
    public String toString() {
        return "AdvertiseMap{" +
                "adProbabilities=" + adProbabilities +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
