package com.chzzkGamble.gamble.roulette.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Roulette {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String channelName;

    private boolean voting = false;

    @CreatedDate
    private LocalDateTime createdAt;

    public Roulette(String channelName) {
        this.channelName = channelName;
    }

    public void startVote() {
        voting = true;
    }

    public void endVote() {
        voting = false;
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
