package com.chzzkGamble.gamble.roulette.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Roulette {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String channelName;

    private boolean voting = false;

    private int rouletteUnit;

    @CreatedDate
    private LocalDateTime createdAt;

    public Roulette(String channelName, int rouletteUnit) {
        this.channelName = channelName;
        validateRouletteUnit(rouletteUnit);
        this.rouletteUnit = rouletteUnit;
    }

    public void startVote() {
        voting = true;
    }

    public void endVote() {
        voting = false;
    }

    public void updateRouletteUnit(int rouletteUnit) {
        validateRouletteUnit(rouletteUnit);
        this.rouletteUnit = rouletteUnit;
    }

    private void validateRouletteUnit(int rouletteUnit) {
        if (rouletteUnit <= 0) {
            throw new IllegalArgumentException("룰렛 단위는 0 이상입니다.");
        }
    }
}
