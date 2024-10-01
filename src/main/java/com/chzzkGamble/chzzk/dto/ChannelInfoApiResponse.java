package com.chzzkGamble.chzzk.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChannelInfoApiResponse {

    private String channelId;
    private String channelName;
    private String channelImageUrl;
    private boolean verifiedMark;

    public boolean isInvalid() {
        return verifiedMark;
    }
}
