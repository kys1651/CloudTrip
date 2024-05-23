package com.ssafy.trip.domain.tour.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleTourWithLike extends SimpleTour {
    private int scheduleCount;
    private int reviewCount;
    private int rating;
}
