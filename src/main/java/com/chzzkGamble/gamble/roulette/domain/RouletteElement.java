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

    private int cheese;

    @ManyToOne
    private Roulette roulette;

    public RouletteElement(String name, int cheese, Roulette roulette) {
        this.name = name;
        this.cheese = cheese;
        this.roulette = roulette;
    }

    public static RouletteElement newInstance(String name, Roulette roulette) {
        return new RouletteElement(name, 0, roulette);
    }

    public void increaseCheese(int cheese) {
        if (cheese <= 0 || this.cheese + cheese < 0) {
            throw new IllegalArgumentException("잘못된 상승 수치입니다. amount : " + cheese);
        }
        this.cheese += cheese;
    }
}
