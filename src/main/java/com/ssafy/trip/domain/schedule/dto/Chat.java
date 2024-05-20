package com.ssafy.trip.domain.schedule.dto;

import com.ssafy.trip.domain.schedule.entity.MessageType;
import lombok.Data;

@Data
public class Chat {
    private MessageType type;
    private Long scheduleId;
    private Long userId;
    private String message;
    private Long createdAt;
}
