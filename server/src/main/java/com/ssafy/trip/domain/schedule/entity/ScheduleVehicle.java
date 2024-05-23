package com.ssafy.trip.domain.schedule.entity;

import lombok.Data;

@Data
public class ScheduleVehicle {
    private Long scheduleId;
    private Long vehicleId;
    private String type;
    private int fromTourId;
    private Integer toTourId;
    private int day;
    private int order;
}
