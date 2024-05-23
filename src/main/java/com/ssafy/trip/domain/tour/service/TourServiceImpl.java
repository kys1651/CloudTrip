package com.ssafy.trip.domain.tour.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.domain.tour.dto.TourData;
import com.ssafy.trip.domain.tour.entity.*;
import com.ssafy.trip.domain.tour.mapper.TourMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourMapper tourMapper;

    @Override
    public List<SimpleTourWithLike> findTourByKeyword(int city, String keyword) {
        return tourMapper.findWithContentByKeyword(city, keyword);
    }

    @Override
    public List<Category> getCategories() {
        return tourMapper.findCategories();
    }

    @Override
    public List<City> getCities() {
        return tourMapper.findCities();
    }

    @Override
    public List<Town> getTowns(int cityCode) {
        return tourMapper.findTowns(cityCode);
    }

    @Override
    public City findCityById(int cityId) {
        return tourMapper.findCityById(cityId).orElseThrow(
                () -> new CustomException(ErrorCode.ITEM_NOT_FOUND)
        );
    }

    @Override
    public List<TourData.Search> findStayByCityId(int cityId) {
        return tourMapper.findStayByCityId(cityId).stream().map(TourData.Search::of).toList();
    }

    @Override
    public Tour findTourById(Long tourId) {
        return tourMapper.findTourById(tourId);
    }
}
