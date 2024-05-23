package com.ssafy.trip.domain.search.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleSearch;
import com.ssafy.trip.domain.search.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ssafy.trip.domain.user.dto.UserData.Profile;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchMapper searchMapper;

    @Override
    public List<ReviewData.Review> searchReviewsByKeyword(String searchKeyword, Long userId) {
        return searchMapper.searchReviewsByKeyword(searchKeyword, userId).stream().map(ReviewData.Review::of).toList();
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
