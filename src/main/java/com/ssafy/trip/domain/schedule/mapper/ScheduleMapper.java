package com.ssafy.trip.domain.schedule.mapper;

import com.ssafy.trip.domain.schedule.entity.Schedule;
import com.ssafy.trip.domain.schedule.entity.ScheduleTrip;
import com.ssafy.trip.domain.schedule.entity.ScheduleVehicle;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    void save(Schedule schedule);

    void addPrivilege(Long scheduleId, String username);

    ScheduleWithUser findById(Long scheduleId);

    List<ScheduleWithUser> findAll();

    void saveBulkScheduleTrip(List<ScheduleTrip> scheduleTrips);

    void finishSchedule(Long scheduleId);

    void saveBulkScheduleVehicle(List<ScheduleVehicle> scheduleVehicles);
}
