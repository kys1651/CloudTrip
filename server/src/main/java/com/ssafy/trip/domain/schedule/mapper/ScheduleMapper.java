package com.ssafy.trip.domain.schedule.mapper;

import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    void save(Schedule schedule);

    void addPrivilege(Long scheduleId, String username);

    ScheduleWithUser findById(Long scheduleId);

    List<ScheduleWithSearch> findAll(ScheduleData.SearchCondition condition);

    List<ScheduleWithSearch> findByMyUserId(Long userId);

    List<ScheduleWithSearch> findByUserId(Long userId);

    List<VehicleDetail> findVehicleDetail(Long scheduleId);

    List<TripWithTour> findTripWithTour(Long scheduleId);

    void updatePublic(Long scheduleId, String publicKey);

    void revokePublic(Long scheduleId);

    void saveBulkScheduleTrip(List<ScheduleTrip> scheduleTrips);

    void finishSchedule(Long scheduleId);

    void saveBulkScheduleVehicle(List<ScheduleVehicle> scheduleVehicles);
}
