package com.ssafy.trip.domain.tour.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TourWithContent extends Tour {
    private String description;
    private String telephone;
    private double latitude;
    private double longitude;
}
