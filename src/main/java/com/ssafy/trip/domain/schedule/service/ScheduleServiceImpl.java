package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.core.service.S3UploadService;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleResponse;
import com.ssafy.trip.domain.schedule.entity.Schedule;
import com.ssafy.trip.domain.schedule.entity.ScheduleTrip;
import com.ssafy.trip.domain.schedule.entity.ScheduleVehicle;
import com.ssafy.trip.domain.schedule.mapper.ScheduleMapper;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleMapper scheduleMapper;
    private final S3UploadService s3UploadService;

    @Override
    public Long createSchedule(
            User user,
            ScheduleData.CreateSchedule schedule,
            MultipartFile image
    ) {
        schedule.setUserId(user.getUserId());
        if (image != null) {
            CompletableFuture<String> future = s3UploadService.upload(image);
            schedule.setThumbnailImage(future.join());
        }
        Schedule created = schedule.toEntity();
        scheduleMapper.save(created);

        if (schedule.isPrivate()) {
            scheduleMapper.addPrivilege(created.getScheduleId(), user.getUsername());
        }

        return created.getScheduleId();
    }

    @Override
    public void createScheduleTrip(Long scheduleId, ScheduleData.CreateScheduleTrip create) {
        List<ScheduleTrip> tripEntities = create.toTripEntity(scheduleId);
        List<ScheduleVehicle> vehicleEntities = create.toVehicleEntity(scheduleId);
        scheduleMapper.saveBulkScheduleTrip(tripEntities);
        scheduleMapper.saveBulkScheduleVehicle(vehicleEntities);
        scheduleMapper.finishSchedule(scheduleId);
    }

    @Override
    public List<ScheduleResponse> searchAllSchedule() {
        return scheduleMapper.findAll().stream().map(ScheduleResponse::from).toList();
    }

    @Override
    public ScheduleResponse searchSchedule(Long scheduleId) {
        return ScheduleResponse.from(scheduleMapper.findById(scheduleId));
    }
}
