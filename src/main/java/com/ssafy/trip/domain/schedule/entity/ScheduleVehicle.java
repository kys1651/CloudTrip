package com.ssafy.trip.domain.schedule.entity;

import lombok.Data;

@Data
public class ScheduleVehicle {
    private Long scheduleId;
    private int vehicleId;
    private String type;
    private int fromTourId;
    private int toTourId;
    private int day;
    private int order;
    private boolean room;
}
