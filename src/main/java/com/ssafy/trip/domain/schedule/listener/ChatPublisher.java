package com.ssafy.trip.domain.schedule.listener;

import com.ssafy.trip.domain.schedule.dto.Chat;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final ChatSubscriber chatSubscriber;
    private final ChannelTopic chatTopic = new ChannelTopic("chat");

    @PostConstruct
    public void init() {
        redisMessageListener.addMessageListener(chatSubscriber, chatTopic);
    }

    public void publish(Chat message) {
        redisTemplate.convertAndSend(chatTopic.getTopic(), message);
    }
}
