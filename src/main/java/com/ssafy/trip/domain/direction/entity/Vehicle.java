package com.ssafy.trip.domain.direction.entity;

import lombok.Data;

import java.util.List;

@Data
public class Vehicle {
    private int vehicleId;
    private int directionId;
    private int fare;
    private int spentTime;
    private int walkTime;
    private int transferCount;
    private int distance;
    private int walkDistance;
    private String path;
    private List<Step> steps;
}
