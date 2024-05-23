package com.ssafy.trip.domain.schedule.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.schedule.dto.Chat;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;
import com.ssafy.trip.domain.schedule.repository.ScheduleChatRepository;
import com.ssafy.trip.domain.schedule.service.ScheduleService;
import com.ssafy.trip.domain.schedule.service.ScheduleTripService;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private final ScheduleChatRepository scheduleChatRepository;
    private final ScheduleService scheduleService;
    private final ScheduleTripService tripService;

    @GetMapping("")
    public SuccessResponse<List<ScheduleData.ScheduleSearch>> getAllSchedule(
            @ModelAttribute ScheduleData.SearchCondition condition
    ) {
        return SuccessResponse.of(scheduleService.searchAllSchedule(condition));
    }

    @GetMapping("/{scheduleId}")
    public SuccessResponse<ScheduleData.ScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        return SuccessResponse.of(scheduleService.searchSchedule(scheduleId));
    }

    @GetMapping("/{scheduleId}/path")
    public SuccessResponse<ScheduleData.PathResponse> getSchedulePath(@PathVariable Long scheduleId) {
        return SuccessResponse.of(scheduleService.searchSchedulePath(scheduleId));
    }

    @PostMapping("/{scheduleId}")
    public SuccessResponse<Void> createScheduleTrip(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleData.CreateScheduleTrip create
    ) {
        scheduleService.createScheduleTrip(scheduleId, create);
        return SuccessResponse.empty();
    }

    @PostMapping("/{scheduleId}/public")
    public SuccessResponse<Void> updateSchedulePublic(@PathVariable Long scheduleId) {
        scheduleService.updateSchedulePublic(scheduleId);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{scheduleId}/revoke")
    public SuccessResponse<Void> removePrivilege(
            @PathVariable Long scheduleId
    ) {
        scheduleService.removePrivilege(scheduleId);
        return SuccessResponse.empty();
    }

    @GetMapping("/{scheduleId}/chat")
    public SuccessResponse<List<Chat>> getChats(@PathVariable Long scheduleId) {
        return SuccessResponse.of(scheduleChatRepository.findAll(scheduleId));
    }

    @PostMapping("/{scheduleId}/invite")
    public SuccessResponse<Void> invite(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleData.Invite invite
    ) {
        scheduleService.invite(scheduleId, invite);
        return SuccessResponse.empty();
    }

    @PostMapping("/{scheduleId}/invite/confirm")
    public SuccessResponse<Void> inviteConfirm(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleData.InviteConfirm inviteConfirm
    ) {
        scheduleService.inviteConfirm(scheduleId, inviteConfirm);
        return SuccessResponse.empty();
    }

    @GetMapping("/{scheduleId}/trip")
    public SuccessResponse<TripAndVehicle> getScheduleTrips(@PathVariable Long scheduleId) {
        return SuccessResponse.of(tripService.findTripAndVehicle(scheduleId));
    }

    @PostMapping("")
    public SuccessResponse<Long> createSchedule(
            @CurrentUser User user,
            ScheduleData.CreateSchedule schedule,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        schedule.setUserId(user.getUserId());
        Long scheduleId = scheduleService.createSchedule(user, schedule, image);
        return SuccessResponse.of(scheduleId);
    }
}
