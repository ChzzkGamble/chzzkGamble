package com.chzzkGamble.videodonation.repository;

import com.chzzkGamble.videodonation.domain.VideoDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDonationRepository extends JpaRepository<VideoDonation, Long> {

}
