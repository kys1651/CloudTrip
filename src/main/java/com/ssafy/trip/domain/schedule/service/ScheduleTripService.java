package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.domain.schedule.dto.TripAndVehicle;

public interface ScheduleTripService {
    void updateTripAndVehicle(TripAndVehicle tripAndVehicle);

    TripAndVehicle findTripAndVehicle(Long scheduleId);
}
