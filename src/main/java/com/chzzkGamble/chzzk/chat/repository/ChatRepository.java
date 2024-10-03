package com.chzzkGamble.chzzk.chat.repository;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Boolean existsByChannelNameAndOpenedIsTrue(String channelName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Chat> findByChannelNameAndOpenedIsTrue(String channelName);
}
