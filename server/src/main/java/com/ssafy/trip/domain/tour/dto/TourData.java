package com.ssafy.trip.domain.tour.dto;

import com.ssafy.trip.domain.tour.entity.TourWithContent;
import lombok.Data;

public class TourData {
    @Data
    public static class Search {
        private int tourId;
        private String contentType;
        private String name;
        private String address;
        private String backgroundImage;
        private String description;
        private double latitude;
        private double longitude;
        private int pickCount;
        private int rating;

        public static Search of(TourWithContent tour) {
            Search search = new Search();
            search.tourId = tour.getTourId();
            search.contentType = tour.getContentType().name();
            search.name = tour.getName();
            search.address = tour.getAddress();
            search.backgroundImage = tour.getBackgroundImage();
            search.description = tour.getDescription();
            search.latitude = tour.getLatitude();
            search.longitude = tour.getLongitude();
            return search;
        }
    }
}
