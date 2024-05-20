package com.ssafy.trip.domain.tour.controller;

import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.tour.dto.TourData;
import com.ssafy.trip.domain.tour.entity.Category;
import com.ssafy.trip.domain.tour.entity.City;
import com.ssafy.trip.domain.tour.entity.Town;
import com.ssafy.trip.domain.tour.entity.City;
import com.ssafy.trip.domain.tour.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @GetMapping("/search")
    public SuccessResponse<List<TourData.Search>> searchKeyword(
            @RequestParam(value = "city", defaultValue = "0") int city,
            @RequestParam(value = "query", required = false) String query
    ) {
        if (!StringUtils.hasText(query)) {
            return SuccessResponse.of(List.of());
        }

        List<TourData.Search> tours = tourService.findTourByKeyword(city, query);
        return SuccessResponse.of(tours);
    }

    @GetMapping("/city/{cityId}")
    public SuccessResponse<City> findCity(@PathVariable("cityId") int cityId) {
        return SuccessResponse.of(tourService.findCityById(cityId));
    }

    @GetMapping("/category")
    public SuccessResponse<List<Category>> categories() {
        return SuccessResponse.of(tourService.getCategories());
    }

    @GetMapping("/{cityId}/stay")
    public SuccessResponse<List<TourData.Search>> getStay(
            @PathVariable("cityId") int cityId
    ) {
        return SuccessResponse.of(tourService.findStayByCityId(cityId));
    }

    @GetMapping("/city")
    public SuccessResponse<List<City>> cities() {
        return SuccessResponse.of(tourService.getCities());
    }

    @GetMapping("/town/{cityCode}")
    public SuccessResponse<List<Town>> towns(@PathVariable("cityCode") int cityCode) {
        log.debug("{}",cityCode);
        return SuccessResponse.of(tourService.getTowns(cityCode));
    }
}
