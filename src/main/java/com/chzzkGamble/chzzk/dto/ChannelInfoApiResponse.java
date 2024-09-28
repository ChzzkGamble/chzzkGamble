package com.chzzkGamble.chzzk.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChannelInfoApiResponse {

    private static final String INVALID_CHANNEL_NAME = "(알 수 없음)";

    private String channelName;
    private String channelImageUrl;

    public boolean isInvalid() {
        return channelName == null || channelName.equals(INVALID_CHANNEL_NAME);
    }
}
