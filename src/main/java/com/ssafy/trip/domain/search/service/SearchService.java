package com.ssafy.trip.domain.search.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.review.entity.Review;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.search.dto.SearchData;
import com.ssafy.trip.domain.user.entity.User;

import java.util.List;

public interface SearchService {

    // Review 검색
    List<ReviewWithUser> searchReviewsByKeyword(String searchKeyword);

    // Notice 검색
    List<Notice> searchNoticesByKeyword(String searchKeyword);

    // User 검색
    List<User> searchUsersByKeyword(String searchKeyword);
}
