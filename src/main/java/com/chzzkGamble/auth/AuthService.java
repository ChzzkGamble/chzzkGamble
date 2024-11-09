package com.chzzkGamble.auth;

import com.chzzkGamble.auth.config.ApiKeyHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

@Service
public class AuthService {

    @Value("${aws.parameter-store.name}")
    private String keyName;

    private final SsmClient ssmClient;

    public AuthService() {
        this.ssmClient = SsmClient.builder().region(Region.AP_NORTHEAST_2).build();
    }

    @PostConstruct
    public void init() {
        updateApiKey();
    }

    public void updateApiKey() {
        GetParameterRequest getParameterRequest = GetParameterRequest.builder().name(keyName).withDecryption(true).build();
        String newApiKey = ssmClient.getParameter(getParameterRequest).parameter().value();
        ApiKeyHolder.setApiKey(newApiKey);
    }
}
