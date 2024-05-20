package com.ssafy.trip.domain.direction.mapper;

import com.ssafy.trip.domain.direction.entity.Direction;
import com.ssafy.trip.domain.direction.entity.Step;
import com.ssafy.trip.domain.direction.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DirectionMapper {
    void saveDirection(Direction direction);

    void saveVehicle(Vehicle vehicle);

    List<Vehicle> findVehicles(int directionId);

    Optional<Direction> findById(int startTourId, int endTourId);

    void saveStep(Step step);
}
