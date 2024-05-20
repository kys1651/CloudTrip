package com.ssafy.trip.domain.direction.service;

import com.ssafy.trip.domain.direction.dto.DirectionData;
import com.ssafy.trip.domain.direction.dto.DirectionData.Request;
import com.ssafy.trip.domain.direction.dto.DirectionData.RequestToLambda;
import com.ssafy.trip.domain.direction.entity.Direction;
import com.ssafy.trip.domain.direction.entity.Vehicle;
import com.ssafy.trip.domain.direction.mapper.DirectionMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class TMapService {
    private final DirectionMapper directionMapper;

    @Value("${AWS_LAMBDA_FUNCTION_URL}")
    private String baseURL;

    @Value("${AWS_LAMBDA_FUNCTION_KAKAO_URL}")
    private String kakaoURL;

    private Vehicle parseAndSaveKakao(RequestToLambda request, int directionId) {
        URI uri = UriComponentsBuilder
                .fromUriString(kakaoURL)
                .encode()
                .build()
                .toUri();

        Vehicle vehicle = new Vehicle();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Car> response = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(request), Car.class);


            vehicle.setSpentTime(Objects.requireNonNull(response.getBody()).getDuration());
            vehicle.setDistance(response.getBody().getDistance());
            vehicle.setDirectionId(directionId);
            vehicle.setPath("car");
            directionMapper.saveVehicle(vehicle);
            return vehicle;
        } catch (Exception e) {
            return vehicle;
        }
    }


    private Vehicle parseWalkVehicle(double distance, int directionId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setSpentTime((int) (distance / 67 * 60));
        vehicle.setDistance((int) distance);
        vehicle.setDirectionId(directionId);
        vehicle.setPath("walk");
        directionMapper.saveVehicle(vehicle);
        return vehicle;
    }

    private Vehicle parseBicycleVehicle(double distance, int directionId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setSpentTime((int) (distance / 227 * 60));
        vehicle.setDistance((int) distance);
        vehicle.setDirectionId(directionId);
        vehicle.setPath("bike");
        directionMapper.saveVehicle(vehicle);
        return vehicle;
    }

    private List<Vehicle> parseAndSave(RequestToLambda request, int directionId) {
        URI uri = UriComponentsBuilder
                .fromUriString(baseURL)
                .encode()
                .build()
                .toUri();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<Vehicle>> response = restTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<List<Vehicle>>() {
            });

            Objects.requireNonNull(response.getBody()).forEach(vehicle -> {
                vehicle.setDirectionId(directionId);
                directionMapper.saveVehicle(vehicle);
                vehicle.getSteps().forEach(step -> {
                    step.setVehicleId(vehicle.getVehicleId());
                    directionMapper.saveStep(step);
                });
            });

            return response.getBody();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public DirectionData.Mobility getMobility(Request request) {
        List<Vehicle> vehicles = getVehicle(request);
        DirectionData.Mobility mobility = new DirectionData.Mobility();
        vehicles.forEach(vehicle -> {
            switch (vehicle.getPath()) {
                case "car":
                    mobility.setCar(vehicle);
                    break;
                case "walk":
                    mobility.setWalk(vehicle);
                    break;
                case "bike":
                    mobility.setBike(vehicle);
                    break;
                case "bus":
                    mobility.setBus(vehicle);
                    break;
                case "metro":
                    mobility.setMetro(vehicle);
                    break;
            }
        });
        return mobility;
    }

    @Transactional
    public List<Vehicle> getVehicle(Request request) {
        AtomicBoolean isNew = new AtomicBoolean(false);
        Direction direction = directionMapper.findById(request.getStartTourId(), request.getEndTourId()).orElseGet(() -> {
            isNew.set(true);
            Direction newDirection = new Direction();
            newDirection.setStartTourId(request.getStartTourId());
            newDirection.setEndTourId(request.getEndTourId());
            directionMapper.saveDirection(newDirection);
            return newDirection;
        });

        if (isNew.get()) {
            List<Vehicle> vehicles = parseAndSave(RequestToLambda.of(request), direction.getDirectionId());
            vehicles.add(parseAndSaveKakao(RequestToLambda.of(request), direction.getDirectionId()));
            double distance = calculateDistance(request) * 1000;
            vehicles.add(parseWalkVehicle(distance, direction.getDirectionId()));
            vehicles.add(parseBicycleVehicle(distance, direction.getDirectionId()));
            return vehicles;
        }

        return directionMapper.findVehicles(direction.getDirectionId());
    }

    private double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double calculateDistance(Request request) {
        double earthRadiusKm = 6371;
        double dLat = toRadians(request.getStartY() - request.getEndY());
        double dLon = toRadians(request.getStartX() - request.getEndX());

        double lat1 = toRadians(request.getStartY());
        double lat2 = toRadians(request.getEndY());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
    }

    @Data
    static class Car {
        private int distance;
        private int duration;
    }
}
