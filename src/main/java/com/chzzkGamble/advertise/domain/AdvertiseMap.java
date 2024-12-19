package com.chzzkGamble.advertise.domain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AdvertiseMap {

    public static final String DEFAULT_ADVERTISE_NAME = "광고 한번 해보세요";
    private static final Advertise DEFAULT_ADVERTISE = new Advertise(DEFAULT_ADVERTISE_NAME, "IMAGE_URL_HERE", 1000L, 10);
    public static final double DEFAULT_ADVERTISE_PROBABILITY = 0.1;

    private final Map<Advertise, Long> adCumulativeCosts;
    private final Map<Advertise, Double> adProbabilities;
    private final long totalAmount;

    private AdvertiseMap(Map<Advertise, Long> adCumulativeCosts, Map<Advertise, Double> adProbabilities, long totalAmount) {
        this.adCumulativeCosts = adCumulativeCosts;
        this.adProbabilities = adProbabilities;
        this.totalAmount = totalAmount;
    }

    public static AdvertiseMap from(List<Advertise> advertises) {
        long totalAmount = 0L;
        Map<Advertise, Long> costMap = new LinkedHashMap<>();
        Map<Advertise, Double> probabilityMap = new HashMap<>();

        for (Advertise advertise : advertises) {
            totalAmount += advertise.getAdjustedCost();
            costMap.put(advertise, totalAmount);
        }
        for (Advertise advertise : advertises) {
            probabilityMap.put(advertise, 1.0 * advertise.getAdjustedCost() / totalAmount);
        }

        return new AdvertiseMap(costMap, probabilityMap, totalAmount);
    }

    public Advertise getRandom() {
        if (totalAmount == 0) return DEFAULT_ADVERTISE;

        long threshold = ThreadLocalRandom.current().nextLong((long) (totalAmount + (totalAmount * DEFAULT_ADVERTISE_PROBABILITY)));
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
