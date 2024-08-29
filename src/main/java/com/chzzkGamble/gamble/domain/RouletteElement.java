package com.chzzkGamble.gamble.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        if (amount <= 0 || count + amount < 0) throw new RuntimeException("잘못된 상승 수치입니다. " + amount);
        count += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouletteElement that = (RouletteElement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
