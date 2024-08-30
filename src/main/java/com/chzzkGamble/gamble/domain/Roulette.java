package com.chzzkGamble.gamble.domain;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roulette {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String channelId;

    private String channelName;

    public Roulette(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roulette roulette = (Roulette) o;
        return Objects.equals(id, roulette.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
