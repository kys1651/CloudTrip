package com.ssafy.trip.domain.tour.entity;

import lombok.Data;

@Data
public class Tour {
    private int tourId;
    private int contentTypeId;
    private ContentType contentType;
    private String name;
    private String address;
    private String zipCode;
    private String backgroundImage;
    private String city;
    private String town;

    public void setContentType(String contentType) {
        this.contentType = ContentType.valueOf(contentType);
    }
}
