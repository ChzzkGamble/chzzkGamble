package com.chzzkGamble.chzzk.nickname.service;

import com.chzzkGamble.chzzk.nickname.domain.ChannelName;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ChzzkNicknameService {

    private final Map<String, ChannelName> nicknames = ChzzkNicknameReader.readNicknameData();

    public String getChannelNameFromNickname(String nickname) {
        if (nicknames.containsKey(nickname)) {
            return nicknames.get(nickname).value();
        }
        return nickname;
    }
}
