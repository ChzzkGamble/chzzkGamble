package com.chzzkGamble.chzzk.nickname.service;

import com.chzzkGamble.chzzk.nickname.domain.ChannelName;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ChzzkNicknameReader {

    private static final String NICKNAME_DATA_PATH = "classpath*:chzzk_nickname*.csv";
    private static final String CHARSET_NAME = "EUC-KR";

    public static Map<String, ChannelName> readNicknameData() {
        Map<String, ChannelName> nicknames = new HashMap<>();
        try (CSVReader csvReader = new CSVReaderBuilder(getNicknameReader()).build()) {
            String[] line = csvReader.readNext(); // drop first row
            while ((line = csvReader.readNext()) != null) {
                ChannelName channelName = new ChannelName(line[2]);
                String nickname = line[3];
                nicknames.put(nickname, channelName);
            }
            return Collections.unmodifiableMap(nicknames);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("닉네임 데이터 파일을 읽어오는데 실패했습니다.", e);
        }
    }

    private static Reader getNicknameReader() throws IOException {
        return new InputStreamReader(getNicknameResource().getInputStream(),
                Charset.forName(CHARSET_NAME));
    }

    private static Resource getNicknameResource() throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(NICKNAME_DATA_PATH);

        return Arrays.stream(resources)
                .filter(resource -> resource.getFilename() != null)
                .max(Comparator.comparing(Resource::getFilename))
                .orElseThrow(() -> new RuntimeException(NICKNAME_DATA_PATH + "를 읽어오는데 실패했습니다."));
    }
}
