package com.chzzkGamble.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentVariables {

    public static String INSTANCE_ID;

    @Value("${INSTANCE_ID:app}")
    private void setInstanceId(String instanceId) {
        INSTANCE_ID = instanceId;
    }
}
