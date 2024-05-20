package com.ssafy.trip.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.trip.domain.review.entity.Comment;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.user.entity.SimpleUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewData {

    @Data
    public static class Create {
        private Long reviewId;
        private String content;
        private int tourId;
        private int rating;
    }

    @Data
    public static class CommentCreate {
        private String content;
    }

    @Data
    public static class CommentResponse {
        private Long commentId;
        private SimpleUser user;
        private String content;
        private LocalDateTime createdAt;

        public static CommentResponse of(Comment comment) {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentId(comment.getCommentId());
            commentResponse.setUser(comment.getUser());
            commentResponse.setContent(comment.getContent());
            commentResponse.setCreatedAt(comment.getCreatedAt());
            return commentResponse;
        }
    }

    @Data
    public static class Update {
        private String content;
        private int tourId;
        private Long reviewId;
    }

    @Data
    public static class Review {
        private Long reviewId;
        @JsonProperty("isLiked")
        private boolean isLiked;
        private String address;
        private String content;
        private String tourName;
        private int tourId;
        private int likeCount;
        private int rating;
        private String createdAt;
        private SimpleUser user;
        private String updatedAt;
        private List<String> images;

        public static Review of(ReviewWithUser review) {
            Review reviewResponse = new Review();
            reviewResponse.setReviewId(review.getReviewId());
            reviewResponse.setContent(review.getContent());
            reviewResponse.setLiked(review.isLiked());
            reviewResponse.setAddress(review.getAddress());
            reviewResponse.setTourName(review.getTourName());
            reviewResponse.setTourId(review.getTourId());
            reviewResponse.setLikeCount(review.getLikeCount());
            reviewResponse.setRating(review.getRating());
            reviewResponse.setCreatedAt(review.getCreatedAt().toString());
            reviewResponse.setUser(review.getUser());
            reviewResponse.setUpdatedAt(review.getUpdatedAt().toString());
            reviewResponse.setImages(review.getImages());
            return reviewResponse;
        }
    }

    @Data
    public static class SimpleReview {
        private Long reviewId;
        private String address;
        private String content;
        @JsonProperty("isLiked")
        private boolean isLiked;
        private int likeCount;
        private int rating;
        private int tourId;
        private String tourName;
        private String createdAt;
        private List<String> images;
        private SimpleUser user;

        public static SimpleReview of(ReviewWithUser review) {
            SimpleReview simpleReview = new SimpleReview();
            simpleReview.setReviewId(review.getReviewId());
            simpleReview.setContent(review.getContent());
            simpleReview.setLiked(review.isLiked());
            simpleReview.setLikeCount(review.getLikeCount());
            simpleReview.setRating(review.getRating());
            simpleReview.setAddress(review.getAddress());
            simpleReview.setTourName(review.getTourName());
            simpleReview.setTourId(review.getTourId());
            simpleReview.setCreatedAt(review.getCreatedAt().toString());
            simpleReview.setImages(review.getImages());
            simpleReview.setUser(review.getUser());
            return simpleReview;
        }
    }

    @Data
    public static class UpdateComment{
        private String content;
    }
}
