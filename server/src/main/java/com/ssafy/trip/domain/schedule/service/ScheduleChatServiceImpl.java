package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.domain.schedule.dto.Chat;
import com.ssafy.trip.domain.schedule.repository.ScheduleChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleChatServiceImpl implements ScheduleChatService {
    private final ScheduleChatRepository chatRepository;

    @Override
    public void sendChat(Chat chat) {
        chatRepository.addChat(chat);
    }

    @Override
    public void removeChat(Chat chat) {
        chatRepository.removeChat(chat);
    }

    @Override
    public List<Chat> getChats(Long scheduleId) {
        return chatRepository.findAll(scheduleId);
    }
}
