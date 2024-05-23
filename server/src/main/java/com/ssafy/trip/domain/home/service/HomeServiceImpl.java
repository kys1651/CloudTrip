package com.ssafy.trip.domain.home.service;

import com.ssafy.trip.domain.home.dto.HomeData;
import com.ssafy.trip.domain.home.dto.HomeData.NumberData;
import com.ssafy.trip.domain.home.mapper.HomeMapper;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.tour.entity.Tour;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ssafy.trip.domain.home.dto.HomeData.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{

    public final HomeMapper homeMapper;

    @Override
    public Optional<NumberData> getTotalNumbers() {
        return homeMapper.getTotalNumbers();
    }

    @Override
    public List<TopTour> getTopTours() {
        return homeMapper.getTopTours();
    }

    @Override
    public List<TopReview> getTopReviews() {
        return homeMapper.getTopReviews();
    }

    @Override
    public TourInfo getTopTourInfos(Long tourId) {
        return homeMapper.getTopTourInfos(tourId);
    }

    @Override
    public ReviewInfo getTopReviewInfos(Long reviewId) {
        return homeMapper.getTopReviewInfos(reviewId);
    }
}
