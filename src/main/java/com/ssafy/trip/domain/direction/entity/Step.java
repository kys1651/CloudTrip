package com.ssafy.trip.domain.direction.entity;

import lombok.Data;

@Data
public class Step {
    private int stepId;
    private int vehicleId;
    private String mode;
    private String startName;
    private String endName;
    private int sectionTime;
    private int distance;
    private String routeName;
}
