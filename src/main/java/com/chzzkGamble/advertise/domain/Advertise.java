package com.chzzkGamble.advertise.domain;

import com.chzzkGamble.config.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Advertise extends BaseEntity {

    private static final int MIN_AD_PERIOD = 1;
    private static final int MAX_AD_PERIOD = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Long cost;

    private boolean active;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Min(value = MIN_AD_PERIOD)
    @Max(value = MAX_AD_PERIOD)
    private Integer adPeriod;

    public Advertise(String name, String imageUrl, Long cost, Integer adPeriod) {
        this(name, imageUrl, cost, null, adPeriod);
    }

    public Advertise(String name, String imageUrl, Long cost, LocalDateTime startDate, Integer adPeriod) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.active = false;
        this.startDate = startDate;
        this.endDate = startDate != null ? startDate.plusDays(adPeriod) : null;
        this.adPeriod = adPeriod;
    }

    public void approval(Clock clock) {
        this.active = true;
        this.startDate = LocalDateTime.now(clock);
        this.endDate = startDate.plusDays(adPeriod);
    }

    public void rejection() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Advertise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", cost=" + cost +
                ", active=" + active +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
