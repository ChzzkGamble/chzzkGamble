package com.chzzkGamble.gamble.domain;

import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roulette {

    @Id
    private UUID id;

    private String channelId;

    public Roulette(UUID id, String channelId) {
        this.id = id;
        this.channelId = channelId;
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
