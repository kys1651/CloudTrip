package com.ssafy.trip.domain.schedule.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.service.EmailService;
import com.ssafy.trip.core.service.S3UploadService;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleResponse;
import com.ssafy.trip.domain.schedule.entity.*;
import com.ssafy.trip.domain.schedule.mapper.ScheduleMapper;
import com.ssafy.trip.domain.user.entity.SimpleUserWithUsername;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleMapper scheduleMapper;
    private final S3UploadService s3UploadService;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    private final UserMapper userMapper;

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
        scheduleMapper.addPrivilege(created.getScheduleId(), user.getUsername());
        return created.getScheduleId();
    }

    @Transactional
    @Override
    public void createScheduleTrip(Long scheduleId, ScheduleData.CreateScheduleTrip create) {
        List<ScheduleTrip> tripEntities = create.toTripEntity(scheduleId);
        List<ScheduleVehicle> vehicleEntities = create.toVehicleEntity(scheduleId);
        System.out.println(tripEntities);
        System.out.println(vehicleEntities);
        scheduleMapper.saveBulkScheduleTrip(tripEntities);
        scheduleMapper.saveBulkScheduleVehicle(vehicleEntities);
        scheduleMapper.finishSchedule(scheduleId);
    }

    @Override
    public List<ScheduleData.ScheduleSearch> searchAllSchedule(ScheduleData.SearchCondition condition) {
        System.out.println(condition);
        return scheduleMapper.findAll(condition).stream().map(ScheduleData.ScheduleSearch::from).toList();
    }

    @Override
    public List<ScheduleData.ScheduleSearch> searchById(Long userId) {
        return scheduleMapper.findByUserId(userId).stream().map(ScheduleData.ScheduleSearch::from).toList();
    }

    @Override
    public void updateSchedulePublic(Long scheduleId) {
        String publicKey = UUID.randomUUID().toString();
        scheduleMapper.updatePublic(scheduleId, publicKey);
    }

    @Override
    public void removePrivilege(Long scheduleId) {
        scheduleMapper.revokePublic(scheduleId);
    }

    @Override
    public void invite(Long scheduleId, ScheduleData.Invite invite) {
        userMapper.findByUsername(invite.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if (scheduleMapper.findById(scheduleId).getInvitedUsers().stream().map(SimpleUserWithUsername::getUsername).toList().contains(invite.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_INVITE_USER);
        }

        String code = String.valueOf((int) (Math.random() * 899999) + 100000);
        redisTemplate.opsForValue().set("schedule-invite:" + scheduleId + ":" + invite.getUsername(), code);
        emailService.sendScheduleInviteEmail(code, invite, scheduleId.toString());
    }

    @Override
    public void inviteConfirm(Long scheduleId, ScheduleData.InviteConfirm inviteConfirm) {
        String code = redisTemplate.opsForValue().get("schedule-invite:" + scheduleId + ":" + inviteConfirm.getEmail());
        if (code == null || !code.equals(inviteConfirm.getCode())) {
            throw new IllegalArgumentException("코드가 일치하지 않습니다.");
        }
        scheduleMapper.addPrivilege(scheduleId, inviteConfirm.getEmail());
    }

    @Override
    public ScheduleResponse searchSchedule(Long scheduleId) {
        return ScheduleResponse.from(scheduleMapper.findById(scheduleId));
    }

    @Override
    public ScheduleData.PathResponse searchSchedulePath(Long scheduleId) {
        List<TripWithTour> tripWithTour = scheduleMapper.findTripWithTour(scheduleId);
        List<VehicleDetail> vehicleDetail = scheduleMapper.findVehicleDetail(scheduleId);
        return ScheduleData.PathResponse.from(tripWithTour, vehicleDetail);
    }
}
