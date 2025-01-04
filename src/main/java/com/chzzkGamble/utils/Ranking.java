package com.chzzkGamble.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Ranking<T> {
    private final int rank;
    private final T element;

    private Ranking(int rank, T element) {
        this.rank = rank;
        this.element = element;
    }

    public static <T> Ranking<T> of(int rank, T element) {
        return new Ranking<>(rank, element);
    }
}
