package com.ssafy.trip.domain.home.service;

import com.ssafy.trip.domain.home.dto.HomeData;
import com.ssafy.trip.domain.home.dto.HomeData.NumberData;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.tour.entity.Tour;

import java.util.List;
import java.util.Optional;

import static com.ssafy.trip.domain.home.dto.HomeData.*;

public interface HomeService {

    // 리뷰 총 개수 조회
    // 공지사항 총 개수 조회
    // 총 사용자 수 조회
    // 총 여행지 수 조회
    // 생성된 여행 계획 수 조회
    Optional<NumberData> getTotalNumbers();

    // Top 여행지 조회
    List<TopTour> getTopTours();
    
    // Top 리뷰 조회
    List<TopReview> getTopReviews();

    // tour_id로 여행지 detail, city, town 정보 가져오기
    TourInfo getTopTourInfos(Long tourId);

    // review_id로 리뷰 content, createdAt, name, image, likeCount 가져오기
    ReviewInfo getTopReviewInfos(Long reviewId);
}
