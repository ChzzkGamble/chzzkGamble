package com.chzzkGamble.advertise.domain;

import com.chzzkGamble.config.BaseEntity;
import com.chzzkGamble.exception.AdvertiseException;
import com.chzzkGamble.exception.AdvertiseExceptionCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Advertise extends BaseEntity {

    public static final int MIN_AD_PERIOD = 1;
    public static final int MAX_AD_PERIOD = 30;

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
        validate(cost, adPeriod);
        this.name = name;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.active = false;
        this.adPeriod = adPeriod;
    }
    
    private void validate(Long cost, Integer adPeriod) {
        validateCost(cost);
        validateAdPeriod(adPeriod);
    }

    private void validateCost(Long cost) {
        if (cost <= 0) {
            throw new AdvertiseException(AdvertiseExceptionCode.COST_UNDER_ZERO);
        }
    }

    private void validateAdPeriod(Integer adPeriod) {
        if (adPeriod < MIN_AD_PERIOD || adPeriod > MAX_AD_PERIOD) {
            throw new AdvertiseException(AdvertiseExceptionCode.INVALID_AD_PERIOD);
        }
    }

    public void approval(Clock clock) {
        if (active) {
            throw new AdvertiseException(AdvertiseExceptionCode.ALREADY_APPROVED_AD);
        }
        active = true;
        startDate = LocalDateTime.now(clock);
        endDate = startDate.plusDays(adPeriod);
    }

    public void rejection() {
        if (!active) {
            throw new AdvertiseException(AdvertiseExceptionCode.AD_NOT_APPROVED);
        }
        active = false;
    }

    public long getAdjustedCost() {
        return cost / adPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advertise advertise = (Advertise) o;
        return Objects.equals(id, advertise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
