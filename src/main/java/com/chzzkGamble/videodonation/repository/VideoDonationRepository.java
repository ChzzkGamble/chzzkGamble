package com.chzzkGamble.videodonation.repository;

import com.chzzkGamble.videodonation.domain.VideoDonation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDonationRepository extends JpaRepository<VideoDonation, Long> {

    List<VideoDonation> findByChannelNameAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(String channelName,
                                                                               LocalDateTime recent);
}
