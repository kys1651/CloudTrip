package com.ssafy.trip.domain.search.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleSearch;
import com.ssafy.trip.domain.schedule.entity.ScheduleWithSearch;
import com.ssafy.trip.domain.search.dto.SearchData;
import com.ssafy.trip.domain.search.mapper.SearchMapper;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ssafy.trip.domain.user.dto.UserData.*;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final SearchMapper searchMapper;
    @Override
    public List<ReviewWithUser> searchReviewsByKeyword(String searchKeyword) {
        return searchMapper.searchReviewsByKeyword(searchKeyword);
    }

    @Override
    public List<Notice> searchNoticesByKeyword(String searchKeyword) {
        return searchMapper.searchNoticesByKeyword(searchKeyword);
    }

    @Override
    public List<Profile> searchUsersByKeyword(String searchKeyword) {
        return searchMapper.searchUsersByKeyword(searchKeyword).stream().map(Profile::of).toList();
    }

    @Override
    public List<ScheduleSearch> searchSchedulesByKeyword(String searchKeyword) {
        return searchMapper.searchSchedulesByKeyword(searchKeyword).stream().map(ScheduleSearch::from).toList();
    }
}
