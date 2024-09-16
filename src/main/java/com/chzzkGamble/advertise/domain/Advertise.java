package com.chzzkGamble.advertise.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Advertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Integer cost;

    private boolean approved;

    @CreatedDate
    private LocalDateTime createdAt;

    public Advertise(String name, String imageUrl, Integer cost, boolean approved) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Advertise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", cost=" + cost +
                ", isApproved=" + approved +
                ", createdAt=" + createdAt +
                '}';
    }
}
