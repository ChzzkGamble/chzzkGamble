package com.chzzkGamble.chzzk.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.chzzkGamble.chzzk.chat.service.ChzzkChatService;
import com.chzzkGamble.chzzk.chat.service.WebSocketConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

// 12/18 기준 local 컴퓨터에 redis를 설치, 실행해야 정상적으로 동작합니다.
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChzzkChatConcurrencyTest {

    @Autowired
    private ChzzkChatService chzzkChatService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private RedissonClient redissonClient;

    @MockBean
    private WebSocketConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        chatRepository.deleteAllInBatch();
    }

    @AfterEach
    void deleteCache() {
        redissonClient.getBucket("테스트").delete();
    }

    @Test
    @DisplayName("같은 채널명에 대한 연결 동시성 테스트")
    void testConnectChatRoomConcurrency() throws InterruptedException {
        // given
        String channelName = "테스트";
        int numberOfThreads = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

        Map<String, Boolean> connectionStates = new ConcurrentHashMap<>();

        doAnswer(invocation -> {
            String mockChannelName = invocation.getArgument(0, String.class);
            connectionStates.put(mockChannelName, true);
            return null;
        }).when(connectionManager).connect(anyString());

        when(connectionManager.isConnected(anyString()))
                .thenAnswer(invocation -> {
                    String mockChannelName = invocation.getArgument(0, String.class);
                    return connectionStates.getOrDefault(mockChannelName, false);
                });

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    startLatch.await();
                    chzzkChatService.connectChatRoom(channelName);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        startLatch.countDown();
        endLatch.await();
        executorService.shutdown();

        // then
        List<Chat> chats = chatRepository.findAll();
        assertAll(
                () -> assertThat(chats).hasSize(1),
                () -> assertThat(chats.get(0).getChannelName()).isEqualTo(channelName)
        );

        verify(connectionManager, times(1)).connect(channelName);
    }
}
