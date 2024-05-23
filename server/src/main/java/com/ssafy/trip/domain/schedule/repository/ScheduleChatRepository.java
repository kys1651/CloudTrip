package com.ssafy.trip.domain.schedule.repository;

import com.ssafy.trip.domain.schedule.dto.Chat;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class ScheduleChatRepository {
    private static final String SCHEDULE_CHATS = "schedule_chats:";
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Chat> opsZSetChat;

    public void addChat(Chat chat) {
        Long scheduleId = chat.getScheduleId();
        opsZSetChat.add(SCHEDULE_CHATS + scheduleId, chat, chat.getCreatedAt());
    }

    public void removeChat(Chat chat) {
        Long scheduleId = chat.getScheduleId();
        opsZSetChat.remove(SCHEDULE_CHATS + scheduleId, chat);
    }

    public List<Chat> findAll(Long scheduleId) {
        Set<Chat> range = opsZSetChat.range(SCHEDULE_CHATS + scheduleId, 0, -1);
        if (Objects.isNull(range)) {
            // TODO: RDBMS 에서 긁어오기
            return List.of();
        }
        return range.stream().toList();
    }
}
