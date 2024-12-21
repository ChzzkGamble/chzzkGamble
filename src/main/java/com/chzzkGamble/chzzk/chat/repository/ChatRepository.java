package com.chzzkGamble.chzzk.chat.repository;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByChannelNameAndOpenedIsTrue(String channelName);
}
