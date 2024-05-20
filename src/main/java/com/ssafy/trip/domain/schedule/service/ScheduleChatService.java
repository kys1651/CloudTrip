package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.domain.schedule.dto.Chat;

import java.util.List;

public interface ScheduleChatService {
    void sendChat(Chat chat);

    void removeChat(Chat chat);

    List<Chat> getChats(Long scheduleId);
}
