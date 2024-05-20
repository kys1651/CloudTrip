package com.ssafy.trip.domain.schedule.listener;

import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulePublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final ScheduleSubscriber scheduleSubscriber;
    private final ChannelTopic scheduleTopic = new ChannelTopic("schedule");

    @PostConstruct
    public void init() {
        redisMessageListener.addMessageListener(scheduleSubscriber, scheduleTopic);
    }

    public void publish(TripAndVehicle tripAndVehicle) {
        redisTemplate.convertAndSend(scheduleTopic.getTopic(), tripAndVehicle);
    }
}
