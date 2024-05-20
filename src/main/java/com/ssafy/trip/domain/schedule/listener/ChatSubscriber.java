package com.ssafy.trip.domain.schedule.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.trip.domain.schedule.dto.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            Chat chat = objectMapper.readValue(publishMessage, Chat.class);
            messagingTemplate.convertAndSend("/sub/schedule/" + chat.getScheduleId() + "/chat", chat);
        } catch (Exception e) {
            log.error("Error occurred while parsing message: {}", e.getMessage());
        }
    }
}
