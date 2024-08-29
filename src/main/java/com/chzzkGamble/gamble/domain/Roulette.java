package com.chzzkGamble.gamble.domain;

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
}
