package com.chzzkGamble.chzzk.chat.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.chzzkGamble.chzzk.chat.domain.Chat;
import com.chzzkGamble.chzzk.chat.repository.ChatRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChzzkChatFacadeTest {

    @Autowired
    private ChzzkChatFacade chzzkChatFacade;

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

        Mockito.doAnswer(invocation -> {
            String mockChannelName = invocation.getArgument(0, String.class);
            connectionStates.put(mockChannelName, true);
            return null;
        }).when(connectionManager).connect(Mockito.anyString());

        Mockito.when(connectionManager.isConnected(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String mockChannelName = invocation.getArgument(0, String.class);
                    return connectionStates.getOrDefault(mockChannelName, false);
                });

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    startLatch.await();
                    chzzkChatFacade.connectChatRoom(channelName);
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
        Assertions.assertAll(
                () -> assertThat(chats).hasSize(1),
                () -> assertThat(chats.get(0).getChannelName()).isEqualTo(channelName)
        );

        Mockito.verify(connectionManager, Mockito.times(1)).connect(channelName);
    }
}
