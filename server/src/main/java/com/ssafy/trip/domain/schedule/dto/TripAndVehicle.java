package com.ssafy.trip.domain.schedule.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TripAndVehicle {
    private int day;
    private List<List<TripPlace>> trips;
    private List<SocketVehicle> vehicles;
    private Long scheduleId;
    private Long userId;

    public static TripAndVehicle init(Long scheduleId, int day) {
        TripAndVehicle tripAndVehicle = new TripAndVehicle();
        tripAndVehicle.setScheduleId(scheduleId);
        tripAndVehicle.setDay(day);
        List<List<TripPlace>> array = new ArrayList<>();
        for (int i = 0; i < day; i++) {
            array.add(new ArrayList<>());
        }
        tripAndVehicle.setVehicles(new ArrayList<>());
        tripAndVehicle.setTrips(array);
        return tripAndVehicle;
    }

    @Data
    public static class TripPlace {
        private Long tourId;
        private double latitude;
        private double longitude;
        private String name;
        private String address;
        private String backgroundImage;
        private boolean room;
    }

    @Data
    public static class TripVehicle {
        private Long vehicleId;
        private String name;
        private String vehicleType;
        private int fromTourId;
        private int toTourId;
    }
}
