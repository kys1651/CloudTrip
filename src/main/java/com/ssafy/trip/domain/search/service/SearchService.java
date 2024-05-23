package com.ssafy.trip.domain.search.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithSearch;
import com.ssafy.trip.domain.search.dto.SearchData;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.entity.User;

import java.util.List;

import static com.ssafy.trip.domain.schedule.dto.ScheduleData.*;
import static com.ssafy.trip.domain.user.dto.UserData.*;

public interface SearchService {

    // Review 검색
    List<ReviewWithUser> searchReviewsByKeyword(String searchKeyword);

    // Notice 검색
    List<Notice> searchNoticesByKeyword(String searchKeyword);

    // User 검색
    List<Profile> searchUsersByKeyword(String searchKeyword);

    // Schedule 검색
    List<ScheduleSearch> searchSchedulesByKeyword(String searchKeyword);
}
