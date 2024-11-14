package com.chzzkGamble.support;

import com.chzzkGamble.chzzk.dto.DonationMessage;
import com.chzzkGamble.event.DonationEvent;
import org.springframework.web.socket.TextMessage;

public class StubDonationEvent extends DonationEvent {

    public StubDonationEvent(String channelName, String msg, int cheese) {
        super(new DonationMessage(channelName, new TextMessage(
                "{\n" +
                        "  \"bdy\": [\n" +
                        "    {\n" +
                        "      \"msg\": \"" + msg + "\",\n" +
                        "      \"extras\": \"payAmount:" + cheese + ",donationType:CHAT\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
        )));
    }
}
