package com.chzzkGamble.videodonation.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.chzzkGamble.exception.YoutubeException;
import com.chzzkGamble.exception.YoutubeExceptionCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class YoutubeErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String errorMessage = extractErrorMessage(response);
        if (errorMessage.contains("\"reason\": \"quotaExceeded\"")) {
            throw new YoutubeException(YoutubeExceptionCode.YOUTUBE_QUOTA_EXCEEDED);
        }

        throw new YoutubeException(YoutubeExceptionCode.YOUTUBE_API_INVALID, errorMessage);
    }

    private String extractErrorMessage(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
