package com.ssafy.trip.domain.tour.service;

import com.ssafy.trip.domain.tour.dto.TourData;
import com.ssafy.trip.domain.tour.entity.*;

import java.util.List;

public interface TourService {
    List<SimpleTourWithLike> findTourByKeyword(int city, String keyword);

    List<Category> getCategories();

    List<City> getCities();

    List<Town> getTowns(int cityCode);

    City findCityById(int cityId);

    List<TourData.Search> findStayByCityId(int cityId);

    // 여행지 조회 -> home에서 사용
    Tour findTourById(Long tourId);
}
