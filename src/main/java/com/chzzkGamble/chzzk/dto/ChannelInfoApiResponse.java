package com.chzzkGamble.chzzk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ChannelInfoApiResponse {

    private String channelId;
    private String channelName;
    private String channelImageUrl;
    private boolean verifiedMark;
    private boolean openLive;
}
