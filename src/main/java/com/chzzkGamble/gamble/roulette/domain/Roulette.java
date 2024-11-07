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
}
