package com.ssafy.trip.domain.schedule.dto;

import com.ssafy.trip.domain.direction.entity.Vehicle;
import com.ssafy.trip.domain.schedule.entity.*;
import com.ssafy.trip.domain.tour.entity.SimpleTour;
import com.ssafy.trip.domain.user.entity.SimpleUser;
import com.ssafy.trip.domain.user.entity.SimpleUserWithUsername;
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
    public static class SearchCondition {
        private LocalDate startDate;
        private LocalDate endDate;
        private String access;
        private String mode;
        private List<String> names;
        private int minCount = 0;
        private int maxCount = 100;
    }

    @Data
    public static class ScheduleSearch {
        private Long scheduleId;
        private String name;
        private SimpleUser user;
        private int cityCode;
        private String cityName;
        private String thumbnailImage;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isMulti;
        private int count;
        private boolean isFinished;
        private int day;
        private List<String> usernames;
        private boolean isPrivate;

        public static ScheduleSearch from(ScheduleWithSearch schedule) {
            ScheduleSearch search = new ScheduleSearch();
            search.setScheduleId(schedule.getScheduleId());
            search.setName(schedule.getName());
            search.setUser(schedule.getUser());
            search.setCityCode(schedule.getCityCode());
            search.setCityName(schedule.getCityName());
            search.setCount(schedule.getCount());
            search.setThumbnailImage(schedule.getThumbnailImage());
            search.setStartDate(schedule.getStartDate());
            search.setEndDate(schedule.getEndDate());
            search.setMulti(schedule.isMulti());
            search.setUsernames(schedule.getUsernames());
            search.setFinished(schedule.isFinished());
            search.setDay(schedule.getDay());
            search.setPrivate(schedule.isPrivate());
            return search;
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
        private String publicKey;
        private boolean isMulti;
        private boolean isFinished;
        private int day;
        private boolean isPrivate;
        private List<SimpleUserWithUsername> invitedUsers;

        public static ScheduleResponse from(ScheduleWithUser schedule) {
            ScheduleResponse response = new ScheduleResponse();
            response.setScheduleId(schedule.getScheduleId());
            response.setName(schedule.getName());
            response.setUser(schedule.getUser());
            response.setFinished(schedule.isFinished());
            response.setCityCode(schedule.getCityCode());
            response.setPublicKey(schedule.getPublicKey());
            response.setCityName(schedule.getCityName());
            response.setThumbnailImage(schedule.getThumbnailImage());
            response.setDay(schedule.getDay());
            response.setStartDate(schedule.getStartDate());
            response.setEndDate(schedule.getEndDate());
            response.setMulti(schedule.isMulti());
            response.setPrivate(schedule.isPrivate());
            response.setInvitedUsers(schedule.getInvitedUsers());
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

        public static ScheduleTripRequest from(ScheduleTrip trip) {
            ScheduleTripRequest request = new ScheduleTripRequest();
            request.setTourId(trip.getTourId());
            request.setDay(trip.getDay());
            request.setOrder(trip.getOrder());
            request.setRoom(trip.isRoom());
            return request;
        }
    }

    @Data
    public static class ScheduleVehicleRequest {
        private Long vehicleId;
        private String type;
        private int fromTourId;
        private Integer toTourId;
        private int day;
        private int order;

        public static ScheduleVehicleRequest from(ScheduleVehicle vehicle) {
            ScheduleVehicleRequest request = new ScheduleVehicleRequest();
            request.setVehicleId(vehicle.getVehicleId());
            request.setType(vehicle.getType());
            request.setFromTourId(vehicle.getFromTourId());
            request.setToTourId(vehicle.getToTourId());
            request.setDay(vehicle.getDay());
            request.setOrder(vehicle.getOrder());
            return request;
        }
    }

    @Data
    public static class ScheduleVehicleResponse {
        private Vehicle vehicle;
        private String type;
        private int fromTourId;
        private Integer toTourId;
        private int day;
        private int order;
    }

    @Data
    public static class ScheduleTripResponse {
        private SimpleTour tour;
        private int day;
        private int order;
        private boolean room;
    }

    @Data
    public static class PathResponse {
        private List<ScheduleTripResponse> trips;
        private List<ScheduleVehicleResponse> vehicles;

        public static PathResponse from(List<TripWithTour> trips, List<VehicleDetail> vehicles) {
            PathResponse response = new PathResponse();
            response.setTrips(trips.stream().map(trip -> {
                ScheduleTripResponse tripResponse = new ScheduleTripResponse();
                tripResponse.setTour(trip.getTour());
                tripResponse.setDay(trip.getDay());
                tripResponse.setOrder(trip.getOrder());
                tripResponse.setRoom(trip.isRoom());
                return tripResponse;
            }).toList());
            response.setVehicles(vehicles.stream().map(vehicle -> {
                ScheduleVehicleResponse vehicleResponse = new ScheduleVehicleResponse();
                vehicleResponse.setVehicle(vehicle.getVehicle());
                vehicleResponse.setType(vehicle.getType());
                vehicleResponse.setFromTourId(vehicle.getFromTourId());
                vehicleResponse.setToTourId(vehicle.getToTourId());
                vehicleResponse.setDay(vehicle.getDay());
                vehicleResponse.setOrder(vehicle.getOrder());
                return vehicleResponse;
            }).toList());
            return response;
        }
    }

    @Data
    public static class Invite {
        private String username;
        private String owner;
        private String name;
    }

    @Data
    public static class InviteConfirm {
        private String email;
        private String code;
    }
}
