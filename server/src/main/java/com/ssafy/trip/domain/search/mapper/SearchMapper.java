package com.ssafy.trip.domain.search.mapper;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithSearch;
import com.ssafy.trip.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    // Review 검색
    List<ReviewWithUser> searchReviewsByKeyword(String searchKeyword, Long userId);

    // Notice 검색
    List<Notice> searchNoticesByKeyword(String searchKeyword);

    // User 검색
    List<User> searchUsersByKeyword(String searchKeyword);

    // Schedule 검색
    List<ScheduleWithSearch> searchSchedulesByKeyword(String searchKeyword);
}
