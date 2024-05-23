package com.ssafy.trip.domain.review.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Review {
    private Long reviewId;
    private boolean isLiked;
    private boolean isFollowing;
    private String address;
    private String content;
    private String tourName;
    private int tourId;
    private int likeCount;
    private int rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
}
