package com.ssafy.trip.domain.tour.mapper;

import com.ssafy.trip.domain.tour.entity.*;
import com.ssafy.trip.domain.tour.entity.Category;
import com.ssafy.trip.domain.tour.entity.City;
import com.ssafy.trip.domain.tour.entity.Tour;
import com.ssafy.trip.domain.tour.entity.TourWithContent;
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

    List<TourWithContent> findWithContentByKeyword(int city, String keyword);

    List<City> findCities();

    List<Town> findTowns(int cityCode);
}
