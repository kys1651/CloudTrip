package com.ssafy.trip.domain.search.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.schedule.dto.ScheduleData.ScheduleSearch;
import com.ssafy.trip.domain.search.service.SearchService;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssafy.trip.domain.user.dto.UserData.Profile;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public SuccessResponse<Map<String, List>> search(
            @CurrentUser User user,
            @RequestParam String searchKeyword
    ) {
        Long userId = user == null ? 0 : user.getUserId();
        // Review
        List<ReviewData.Review> reviews = searchService.searchReviewsByKeyword(searchKeyword, userId);
        // Notice
        List<Notice> notices = searchService.searchNoticesByKeyword(searchKeyword);
        // User
        List<Profile> users = searchService.searchUsersByKeyword(searchKeyword);
        // Schedule
        List<ScheduleSearch> schedules = searchService.searchSchedulesByKeyword(searchKeyword);

        HashMap<String, List> hashMap = new HashMap<>();
        hashMap.put("reviews", reviews);
        hashMap.put("notices", notices);
        hashMap.put("users", users);
        hashMap.put("schedules", schedules);

        return SuccessResponse.of(hashMap);
    }
}
