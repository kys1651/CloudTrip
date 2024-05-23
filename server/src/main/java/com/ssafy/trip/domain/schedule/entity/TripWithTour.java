package com.ssafy.trip.domain.schedule.entity;

import com.ssafy.trip.domain.tour.entity.SimpleTour;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TripWithTour extends ScheduleTrip {
    private SimpleTour tour;
}
