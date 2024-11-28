package com.chzzkGamble.advertise.repository;

import com.chzzkGamble.advertise.domain.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    @Query("SELECT a FROM Advertise a WHERE a.active = true AND a.startDate <= :validDateTime AND a.endDate >= :validDateTime")
    List<Advertise> findActiveAdvertisesWithinDate(@Param("validDateTime") LocalDateTime validDateTime);

    List<Advertise> findByActiveFalse();
}
