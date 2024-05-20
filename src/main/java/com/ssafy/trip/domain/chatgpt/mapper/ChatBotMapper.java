package com.ssafy.trip.domain.chatgpt.mapper;

import com.ssafy.trip.domain.chatgpt.dto.ChatGPTData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatBotMapper {
    void save(ChatGPTData.Message message, Long userId);

    List<ChatGPTData.Message> getList(Long userId);

    void deleteChat(Long userId);
}
