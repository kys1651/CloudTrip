package com.ssafy.trip.domain.direction.dto;

import com.ssafy.trip.domain.direction.entity.Vehicle;
import lombok.Data;

public class DirectionData {
    @Data
    public static class Mobility {
        private Vehicle car;
        private Vehicle walk;
        private Vehicle bike;
        private Vehicle bus;
        private Vehicle metro;
    }

    @Data
    public static class Request {
        private int startTourId;
        private double startX;
        private double startY;
        private int endTourId;
        private double endX;
        private double endY;
    }

    @Data
    public static class RequestToLambda {
        private double startX;
        private double startY;
        private double endX;
        private double endY;

        public static RequestToLambda of(Request request) {
            RequestToLambda requestToLambda = new RequestToLambda();
            requestToLambda.setStartX(request.getStartX());
            requestToLambda.setStartY(request.getStartY());
            requestToLambda.setEndX(request.getEndX());
            requestToLambda.setEndY(request.getEndY());
            return requestToLambda;
        }
    }
}
