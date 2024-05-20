package com.ssafy.trip.domain.chatgpt.service;

import com.ssafy.trip.domain.chatgpt.dto.ChatGPTData;

import java.util.List;

public interface ChatGPTService {

    String getRecommend(ChatGPTData.Recommend recommend);

    void sendMessage(ChatGPTData.Message message, Long userId);

    List<ChatGPTData.Message> getList(Long userId);

    void deleteChat(Long userId);
}
