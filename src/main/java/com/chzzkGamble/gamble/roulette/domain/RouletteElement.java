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

    public void increaseCount(int amount) {
        if (amount <= 0 || count + amount < 0) {
            throw new IllegalArgumentException("잘못된 상승 수치입니다. amount : " + amount);
        }
        count += amount;
    }
}
