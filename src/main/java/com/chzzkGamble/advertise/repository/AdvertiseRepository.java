package com.chzzkGamble.advertise.repository;

import com.chzzkGamble.advertise.domain.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    List<Advertise> findByCreatedAtAfterAndApprovedIsTrue(LocalDateTime validDateTime);
}
