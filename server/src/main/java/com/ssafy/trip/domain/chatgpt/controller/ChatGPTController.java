package com.ssafy.trip.domain.chatgpt.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.chatgpt.dto.ChatGPTData.Message;
import com.ssafy.trip.domain.chatgpt.dto.ChatGPTData.Recommend;
import com.ssafy.trip.domain.chatgpt.service.ChatGPTService;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/review")
    public SuccessResponse<String> getRecommend(@RequestBody Recommend recommend) {
        return SuccessResponse.of(chatGPTService.getRecommend(recommend));
    }

    @GetMapping("/chat")
    public SuccessResponse<List<Message>> getList(@CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return SuccessResponse.of(chatGPTService.getList(user.getUserId()));
    }

    @PostMapping("/chat")
    public SuccessResponse<String> sendMessage(@RequestBody Message message, @CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        chatGPTService.sendMessage(message, user.getUserId());
        return SuccessResponse.of(message.getAiResponse());
    }

    @DeleteMapping("/chat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> deleteChat(@CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        chatGPTService.deleteChat(user.getUserId());
        return SuccessResponse.empty();
    }
}
