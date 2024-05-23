package com.ssafy.trip.domain.home.dto;

import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.tour.entity.Tour;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class HomeData {

    private Optional<NumberData> numberData;
    private List<TourInfo> topTours;
    private List<ReviewInfo> topReviews;

    @Data
    public static class NumberData {
        private Long reviewCount;
        private Long noticeCount;
        private Long usersCount;
        private Long tourCount;
        private Long scheduleCount;
    }

    @Data
    public static class TopTour {
        private Long tourId;
        private Double avgRating;
    }

    @Data
    public static class TourInfo {
        private Long tourId;
        private String name;
        private String description;
        private String cityName;
        private String townName;
        private Double rating;
        private String backgroundImage;
    }

    @Data
    public static class TopReview {
        private Long reviewId;
    }

    @Data
    public static class ReviewInfo {
        private Long reviewId;
        private String content;
        private String createdAt;
        private String name;
        private String image;
        private Long likeCount;
    }
}
