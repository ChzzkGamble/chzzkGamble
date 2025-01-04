package com.chzzkGamble.utils;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class RankAssigner {

    private RankAssigner() {
    }

    public static <T, R extends Comparable<R>> List<Ranking<T>> assignRanking(Collection<T> collection, Function<T, R> valueExtractor) {
        List<Element<T, R>> elements = wrapToElement(collection, valueExtractor);
        return getRankings(elements);
    }

    private static <T, R extends Comparable<R>> List<Element<T, R>> wrapToElement(Collection<T> collection, Function<T, R> valueExtractor) {
        return collection.stream()
                .map(c -> new Element<>(c, valueExtractor.apply(c)))
                .sorted(Comparator.comparing((Element<T, R> e) -> e.value).reversed())
                .toList();
    }

    private static <T, R> List<Ranking<T>> getRankings(List<Element<T, R>> elements) {
        List<Ranking<T>> rankings = new ArrayList<>();
        int rank = 1;
        int rankWeight = 1;

        Element<T, R> prevElement = elements.get(0);
        rankings.add(Ranking.of(rank, prevElement.t));

        for (int i = 1; i < elements.size(); i++) {
            Element<T, R> element = elements.get(i);
            if (prevElement.value == element.value) {
                rankWeight++;
            } else {
                rank += rankWeight;
                rankWeight = 1;
            }

            prevElement = element;
            rankings.add(Ranking.of(rank, element.t));
        }
        return rankings;
    }

    private static class Element<T, R> {
        final T t;
        final R value;

        private Element(T t, R value) {
            this.t = t;
            this.value = value;
        }
    }
}
