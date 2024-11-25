package com.chzzkGamble.advertise.domain;

import com.chzzkGamble.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Advertise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Long cost;

    private boolean active;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer adPeriod;

    public Advertise(String name, String imageUrl, Long cost, Integer adPeriod) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.active = false;
        this.adPeriod = adPeriod;
    }

    public Advertise(String name, String imageUrl, Long cost, LocalDateTime startDate, Integer adPeriod) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.active = false;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(adPeriod);
        this.adPeriod = adPeriod;
    }

    public void approval() {
        this.active = true;
        startDate = LocalDateTime.now();
        endDate = LocalDateTime.now().plusDays(10);
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
