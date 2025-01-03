package com.chzzkGamble.videodonation.repository;

import com.chzzkGamble.videodonation.domain.VideoDonation;
import java.time.LocalDateTime;
import java.util.List;
import com.chzzkGamble.videodonation.dto.VideoDonationRankingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDonationRepository extends JpaRepository<VideoDonation, Long> {

    List<VideoDonation> findByChannelNameAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(String channelName,
                                                                                          LocalDateTime recent);

    @Query(value = "SELECT new com.chzzkGamble.videodonation.dto.VideoDonationRankingResponse(v.videoName, v.videoId, SUM(v.cheese), COUNT(v.videoId)) " +
            "FROM VideoDonation v " +
            "WHERE v.videoId is not null " +
            "AND v.createdAt > :from " +
            "AND v.createdAt <= :to " +
            "GROUP BY v.videoId, v.videoName " +
            "ORDER BY SUM(v.cheese) DESC ")
    List<VideoDonationRankingResponse> findRankingByCheese(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query(value = "SELECT new com.chzzkGamble.videodonation.dto.VideoDonationRankingResponse(v.videoName, v.videoId, SUM(v.cheese), COUNT(v.videoId)) " +
            "FROM VideoDonation v " +
            "WHERE v.videoId is not null " +
            "AND v.createdAt > :from " +
            "AND v.createdAt <= :to " +
            "GROUP BY v.videoId, v.videoName " +
            "ORDER BY COUNT(v.videoId) DESC ")
    List<VideoDonationRankingResponse> findRankingByCount(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query(value = "SELECT new com.chzzkGamble.videodonation.dto.VideoDonationRankingResponse(v.videoName, v.videoId, SUM(v.cheese), COUNT(v.videoId)) " +
            "FROM VideoDonation v " +
            "WHERE v.videoId is not null " +
            "AND v.createdAt > :from " +
            "AND v.createdAt <= :to " +
            "GROUP BY v.videoId, v.videoName ")
    List<VideoDonationRankingResponse> findVideoDonations(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

}
