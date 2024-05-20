package com.ssafy.trip.domain.search.controller;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.search.service.SearchService;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<Map<String, List>> search(@RequestParam String searchKeyword) {
        // Review
        List<ReviewWithUser> reviews = searchService.searchReviewsByKeyword(searchKeyword);
        // Notice
        List<Notice> notices = searchService.searchNoticesByKeyword(searchKeyword);
        // User
        List<User> users = searchService.searchUsersByKeyword(searchKeyword);

        HashMap<String, List> hashMap = new HashMap<>();
        hashMap.put("reviews", reviews);
        hashMap.put("notices", notices);
        hashMap.put("users", users);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }
}
