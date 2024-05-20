package com.ssafy.trip.domain.schedule.dto;

import com.ssafy.trip.domain.schedule.entity.Schedule;
import com.ssafy.trip.domain.schedule.entity.ScheduleTrip;
import com.ssafy.trip.domain.schedule.entity.ScheduleVehicle;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithUser;
import com.ssafy.trip.domain.user.entity.SimpleUser;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class ScheduleData {
    @Data
    public static class CreateRoom {
        private String name;
        private String thumbnailImage;
        private boolean isPrivate;
        private String password;
    }

    @Data
    public static class CreateSchedule {
        private String name;
        private Long userId;
        private int cityCode;
        private String thumbnailImage;
        private LocalDate startDate;
        private LocalDate endDate;
        private int day;
        private boolean isMulti = false;
        private boolean isPrivate = false;

        public Schedule toEntity() {
            Schedule schedule = new Schedule();
            schedule.setName(name);
            schedule.setUserId(userId);
            schedule.setCityCode(cityCode);
            schedule.setDay(day);
            schedule.setThumbnailImage(thumbnailImage);
            schedule.setStartDate(startDate);
            schedule.setEndDate(endDate);
            schedule.setMulti(isMulti);
            schedule.setPrivate(isPrivate);
            return schedule;
        }
    }

    @Data
    public static class ScheduleResponse {
        private Long scheduleId;
        private String name;
        private SimpleUser user;
        private int cityCode;
        private String cityName;
        private String thumbnailImage;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isMulti;
        private int day;
        private boolean isPrivate;
        private List<String> usernames;

        public static ScheduleResponse from(ScheduleWithUser schedule) {
            ScheduleResponse response = new ScheduleResponse();
            response.setScheduleId(schedule.getScheduleId());
            response.setName(schedule.getName());
            response.setUser(schedule.getUser());
            response.setCityCode(schedule.getCityCode());
            response.setCityName(schedule.getCityName());
            response.setThumbnailImage(schedule.getThumbnailImage());
            response.setDay(schedule.getDay());
            response.setStartDate(schedule.getStartDate());
            response.setEndDate(schedule.getEndDate());
            response.setMulti(schedule.isMulti());
            response.setPrivate(schedule.isPrivate());
            response.setUsernames(schedule.getUsernames());
            return response;

        }
    }

    @Data
    public static class CreateScheduleTrip {
        private List<ScheduleTripRequest> trips;
        private List<ScheduleVehicleRequest> vehicles;

        public List<ScheduleTrip> toTripEntity(Long scheduleId) {
            return trips.stream().map(trip -> {
                ScheduleTrip scheduleTrip = new ScheduleTrip();
                scheduleTrip.setScheduleId(scheduleId);
                scheduleTrip.setTourId(trip.getTourId());
                scheduleTrip.setDay(trip.getDay());
                scheduleTrip.setOrder(trip.getOrder());
                scheduleTrip.setRoom(trip.isRoom());
                return scheduleTrip;
            }).toList();
        }

        public List<ScheduleVehicle> toVehicleEntity(Long scheduleId) {
            return vehicles.stream().map(vehicle -> {
                ScheduleVehicle scheduleVehicle = new ScheduleVehicle();
                scheduleVehicle.setScheduleId(scheduleId);
                scheduleVehicle.setVehicleId(vehicle.getVehicleId());
                scheduleVehicle.setType(vehicle.getType());
                scheduleVehicle.setFromTourId(vehicle.getFromTourId());
                scheduleVehicle.setToTourId(vehicle.getToTourId());
                scheduleVehicle.setDay(vehicle.getDay());
                scheduleVehicle.setOrder(vehicle.getOrder());
                scheduleVehicle.setRoom(vehicle.isRoom());
                return scheduleVehicle;
            }).toList();
        }

    }

    @Data
    public static class ScheduleTripRequest {
        private int tourId;
        private int day;
        private int order;
        private boolean room;
    }

    @Data
    public static class ScheduleVehicleRequest {
        private int vehicleId;
        private String type;
        private int fromTourId;
        private int toTourId;
        private int day;
        private int order;
        private boolean room;
    }

}
