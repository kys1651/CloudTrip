package com.ssafy.trip.domain.schedule.dto;

import com.ssafy.trip.domain.schedule.entity.ScheduleVehicle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SocketVehicle extends ScheduleVehicle {
    private String name;
}
