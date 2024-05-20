package com.ssafy.trip.domain.direction.controller;

import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.direction.dto.DirectionData;
import com.ssafy.trip.domain.direction.service.TMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/direction")
public class DirectionController {
    private final TMapService tMapService;

    @PostMapping("")
    public SuccessResponse<DirectionData.Mobility> getVehicle(@RequestBody DirectionData.Request request) {
        return SuccessResponse.of(tMapService.getMobility(request));
    }
}
