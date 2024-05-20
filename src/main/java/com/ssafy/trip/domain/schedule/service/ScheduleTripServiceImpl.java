package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;
import com.ssafy.trip.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleTripServiceImpl implements ScheduleTripService {
    private final ScheduleRepository tripRepository;

    @Override
    public void updateTripAndVehicle(TripAndVehicle tripAndVehicle) {
        tripRepository.update(tripAndVehicle);
    }

    @Override
    public TripAndVehicle findTripAndVehicle(Long scheduleId) {
        return tripRepository.findTripAndVehicle(scheduleId);
    }
}
