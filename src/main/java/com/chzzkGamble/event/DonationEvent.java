package com.chzzkGamble.event;

import com.chzzkGamble.chzzk.dto.DonationMessage;
import org.springframework.context.ApplicationEvent;

public class DonationEvent extends ApplicationEvent {

    public DonationEvent(DonationMessage donationMessage) {
        super(donationMessage);
    }
}
