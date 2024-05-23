package com.ssafy.trip.domain.search.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.dto.ReviewData;

import java.util.List;

import static com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleSearch;
import static com.ssafy.trip.domain.user.dto.UserData.Profile;

public interface SearchService {

    // Review 검색
    List<ReviewData.Review> searchReviewsByKeyword(String searchKeyword, Long userId);

    // Notice 검색
    List<Notice> searchNoticesByKeyword(String searchKeyword);

    // User 검색
    List<Profile> searchUsersByKeyword(String searchKeyword);

    // Schedule 검색
    List<ScheduleSearch> searchSchedulesByKeyword(String searchKeyword);
}
