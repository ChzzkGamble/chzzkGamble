package com.chzzkGamble.chzzk.chat.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChzzkChatFacadeTest {

    @Autowired
    private ChzzkChatService chzzkChatService;

    @Autowired
    private ChatRepository chatRepository;

    @MockBean
    private WebSocketConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        chatRepository.deleteAllInBatch();
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
