package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.CreateSchedule;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleResponse;
import com.ssafy.trip.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScheduleService {
    Long createSchedule(User user, CreateSchedule schedule, MultipartFile image);

    void createScheduleTrip(Long scheduleId, ScheduleData.CreateScheduleTrip create);

    List<ScheduleResponse> searchAllSchedule();

    ScheduleResponse searchSchedule(Long scheduleId);

}
