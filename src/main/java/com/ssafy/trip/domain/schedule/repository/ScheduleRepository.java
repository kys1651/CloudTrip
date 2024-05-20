package com.ssafy.trip.domain.schedule.repository;

import com.google.gson.Gson;
import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithUser;
import com.ssafy.trip.domain.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class ScheduleRepository {
    private static final String SCHEDULE_TRIPS = "schedule_trips:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ScheduleMapper scheduleMapper;

    private final Gson format = new Gson();

    public void update(TripAndVehicle tripAndVehicle) {
        String tripJson = format.toJson(tripAndVehicle);
        redisTemplate.opsForValue().set(SCHEDULE_TRIPS + tripAndVehicle.getScheduleId(), tripJson);
    }

    public TripAndVehicle findTripAndVehicle(Long scheduleId) {
        Object trip = redisTemplate.opsForValue().get(SCHEDULE_TRIPS + scheduleId);
        if (Objects.isNull(trip)) {
            ScheduleWithUser schedule = scheduleMapper.findById(scheduleId);
            TripAndVehicle instance = TripAndVehicle.init(scheduleId, schedule.getDay());
            this.update(instance);
            return instance;
        }
        return format.fromJson(trip.toString(), TripAndVehicle.class);
    }
}
