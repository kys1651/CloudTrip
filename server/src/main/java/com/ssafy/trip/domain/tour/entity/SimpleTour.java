package com.ssafy.trip.domain.tour.entity;

import lombok.Data;

@Data
public class SimpleTour {
    private Long tourId;
    private String name;
    private int contentTypeId;
    private String backgroundImage;
    private double latitude;
    private double longitude;
    private String address;
}
