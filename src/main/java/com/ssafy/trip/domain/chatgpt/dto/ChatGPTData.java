package com.ssafy.trip.domain.chatgpt.dto;

import lombok.Data;

public class ChatGPTData {

    @Data
    public static class Recommend {
        String location;
        String content;
    }

    @Data
    public static class Message {
        String userRequest;
        String aiResponse;
    }

    @Data
    public static class Request {
        String prompt;
        String userMessage;
    }
}
