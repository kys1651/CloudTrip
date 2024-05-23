package com.ssafy.trip.domain.direction.entity;

import lombok.Data;

@Data
public class Direction {
    private int directionId;
    private int startTourId;
    private int endTourId;
}
