package com.ssafy.trip.domain.schedule.controller;

import com.ssafy.trip.domain.schedule.dto.Chat;
import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;
import com.ssafy.trip.domain.schedule.entity.MessageType;
import com.ssafy.trip.domain.schedule.listener.ChatPublisher;
import com.ssafy.trip.domain.schedule.listener.SchedulePublisher;
import com.ssafy.trip.domain.schedule.service.ScheduleChatService;
import com.ssafy.trip.domain.schedule.service.ScheduleTripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ScheduleSocketController {
    private final ScheduleChatService chatService;
    private final ScheduleTripService tripService;
    private final ChatPublisher chatPublisher;
    private final SchedulePublisher schedulePublisher;

    @MessageMapping("/schedule/{scheduleId}/chat")
    public void message(
            @DestinationVariable("scheduleId") Long scheduleId,
            Chat message
    ) {
        if (Objects.requireNonNull(message.getType()) == MessageType.TALK) {
            chatService.sendChat(message);
        }
        chatPublisher.publish(message);
    }

    @MessageMapping("/schedule/{scheduleId}/update")
    public void update(
            @DestinationVariable("scheduleId") Long scheduleId,
            TripAndVehicle tripAndVehicle
    ) {
        System.out.println(tripAndVehicle.getVehicles());
        tripService.updateTripAndVehicle(tripAndVehicle);
        schedulePublisher.publish(tripAndVehicle);
    }
}
