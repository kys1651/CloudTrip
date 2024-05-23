package com.ssafy.trip.domain.tour.mapper;

import com.ssafy.trip.domain.tour.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TourMapper {
    List<Tour> findAll();

    Optional<City> findCityById(int cityId);

    List<Category> findCategories();

    Tour findById(int tourId);

    List<TourWithContent> findStayByCityId(int cityId);

    List<SimpleTourWithLike> findWithContentByKeyword(int city, String keyword);

    List<City> findCities();

    List<Town> findTowns(int cityCode);

    // 여행지 조회 -> home에서 사용
    Tour findTourById(Long tourId);
}
