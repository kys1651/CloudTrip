package com.ssafy.trip.domain.home.controller;

import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.home.dto.HomeData;
import com.ssafy.trip.domain.home.service.HomeService;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.review.service.ReviewService;
import com.ssafy.trip.domain.tour.entity.Tour;
import com.ssafy.trip.domain.tour.service.TourService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ssafy.trip.domain.home.dto.HomeData.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
public class HomeController {
    private final HomeService homeService;
    private final ReviewService reviewService;
    private final TourService tourService;

    @GetMapping("")
    public SuccessResponse<HomeData> getHomeData() {

        Optional<NumberData> numberData = homeService.getTotalNumbers();

        // 인기많은 8개의 여행지 정보 받아옴
        List<TopTour> topTours = homeService.getTopTours();

        List<TourInfo> tourList = new ArrayList<>();
        // 해당 tour_id로 tour 정보 조회
        for (TopTour topTour : topTours) {
            TourInfo tourInfo = homeService.getTopTourInfos(topTour.getTourId());
            System.out.println(tourInfo);
            tourInfo.setRating(topTour.getAvgRating());

            tourList.add(tourInfo);
        }

        // 좋아요 수가 많은 8개의 리뷰 정보 받아옴
        List<TopReview> topReviews = homeService.getTopReviews();

        List<ReviewInfo> reviewList = new ArrayList<>();
        // 해당 review_id로 review 정보 조회
        for (TopReview topReview : topReviews) {
            ReviewInfo reviewInfo = homeService.getTopReviewInfos(topReview.getReviewId());
            System.out.println(reviewInfo);

            reviewList.add(reviewInfo);
        }

        HomeData homeData = new HomeData();

        homeData.setNumberData(numberData);
        homeData.setTopTours(tourList);
        homeData.setTopReviews(reviewList);

        return SuccessResponse.of(homeData);
    }
}
