package com.chzzkGamble.gamble.roulette.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class RouletteElement {

    private static final int CHEESE_UNIT = 1_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer count;

    @ManyToOne
    private Roulette roulette;

    public RouletteElement(String name, Integer count, Roulette roulette) {
        this.name = name;
        this.count = count;
        this.roulette = roulette;
    }

    public static RouletteElement newInstance(String name, Roulette roulette) {
        return new RouletteElement(name, 0, roulette);
    }

    public void increaseCount(int cheese) {
        int increaseCount = cheese / CHEESE_UNIT;
        if (increaseCount <= 0 || count + increaseCount < 0) {
            throw new IllegalArgumentException("잘못된 상승 수치입니다. amount : " + increaseCount);
        }
        count += increaseCount;
    }
}
