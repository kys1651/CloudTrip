package com.ssafy.trip.domain.schedule.entity;

import com.ssafy.trip.domain.direction.entity.Vehicle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VehicleDetail extends ScheduleVehicle {
    private Vehicle vehicle;
}
