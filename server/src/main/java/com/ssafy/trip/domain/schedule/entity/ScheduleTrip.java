package com.ssafy.trip.domain.schedule.entity;

import lombok.Data;

@Data
public class ScheduleTrip {
    private Long scheduleId;
    private int tourId;
    private int day;
    private int order;
    private boolean room;
}
