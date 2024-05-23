package com.ssafy.trip.domain.user.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.auth.service.AuthService;
import com.ssafy.trip.domain.review.entity.Comment.SimpleComment;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import com.ssafy.trip.domain.schedule.service.ScheduleService;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.dto.UserData.Password;
import com.ssafy.trip.domain.user.dto.UserData.Update;
import com.ssafy.trip.domain.user.entity.SimpleUser;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final ScheduleService scheduleService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<UserData.LoginUser> me(@CurrentUser User user) {
        if (user == null) {
            return SuccessResponse.of(UserData.LoginUser.unauthenticated());
        }
        return SuccessResponse.of(UserData.LoginUser.authenticated(user));
    }

    @GetMapping("/{userId}")
    public SuccessResponse<UserData.SimpleProfile> findById(
            @PathVariable("userId") Long userId,
            @CurrentUser User user) {
        return SuccessResponse.of(userService.findById(userId, user.getUserId()));
    }

    @GetMapping("")
    public SuccessResponse<List<User>> getAllUser() {
        List<User> list = userService.findAll();
        return SuccessResponse.of(list);
    }

    // 특정 회원 조회
    @GetMapping("/search")
    public SuccessResponse<List<User>> getUserByKeyword(@RequestParam String keyword) {
        List<User> list = userService.findByKeyword(keyword);
        return SuccessResponse.of(list);
    }

    @PatchMapping("/{userId}")
    public SuccessResponse<Void> updateIsLocked(@PathVariable Long userId) {
        userService.updateIsLocked(userId);
        return SuccessResponse.empty();
    }

    @PatchMapping("/me")
    public SuccessResponse<Void> update(
            Update update,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @CurrentUser User user
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        userService.update(update, profileImage, user);
        return SuccessResponse.empty();
    }

    @PatchMapping("/updatePassword")
    public SuccessResponse<Void> updatePassword(
            @RequestBody Password password,
            @CurrentUser User user
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        userService.updatePassword(password, user);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> delete(
            @PathVariable("userId") Long userId,
            @CurrentUser User user,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        Cookie requestCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN, "refresh Token이 존재하지 않습니다."));
        authService.signOut(user, requestCookie.getValue());
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        userService.delete(user);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/admin/{userId}")
    public SuccessResponse<Void> dropUser(
            @CurrentUser User user,
            @PathVariable Long userId) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        userService.drop(userId);
        return SuccessResponse.empty();
    }

    @GetMapping("/{userId}/comments")
    public SuccessResponse<List<SimpleComment>> commentsByUserId(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(userService.commentsByUserId(userId));
    }

    @GetMapping("/{userId}/reviews")
    public SuccessResponse<List<UserData.SimpleReview>> getReviewsById(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(userService.getReviewsById(userId));
    }

    @GetMapping("/{userId}/likes")
    public SuccessResponse<List<UserData.SimpleReview>> getLikedReviews(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(userService.getLikedReviewsById(userId));
    }


    @GetMapping("/{userId}/check")
    public SuccessResponse<Boolean> isFollow(
            @PathVariable("userId") Long followeeId,
            @CurrentUser User user) {
        return SuccessResponse.of(userService.isFollow(followeeId, user.getUserId()));
    }

    @PostMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> followUser(
            @PathVariable("userId") Long followeeId,
            @CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        userService.followUser(followeeId, user.getUserId());
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{userId}/unfollow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> unfollowUser(
            @PathVariable("userId") Long followeeId,
            @CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        userService.unFollowUser(followeeId, user.getUserId());
        return SuccessResponse.empty();
    }

    // 해당유저를 팔로우하는 유저리스트
    @GetMapping("/{userId}/following")
    public SuccessResponse<List<SimpleUser>> getFollowing(
            @PathVariable("userId") Long userId,
            @CurrentUser User user
    ) {
        return SuccessResponse.of(userService.getFollowing(userId, user.getUserId()));
    }

    // 자신을 팔로우하는 유저리스트
    @GetMapping("/{userId}/follower")
    public SuccessResponse<List<SimpleUser>> getFollowers(
            @PathVariable("userId") Long userId,
            @CurrentUser User user) {
        return SuccessResponse.of(userService.getFollowers(userId, user.getUserId()));
    }

    @GetMapping("/{userId}/followerCount")
    public SuccessResponse<Integer> getFollowerCount(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(userService.getFollowerCount(userId));
    }

    @GetMapping("/{userId}/followingCount")
    public SuccessResponse<Integer> getFollowingCount(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(userService.getFollowingCount(userId));
    }

    @GetMapping("/{userId}/schedule")
    public SuccessResponse<List<ScheduleData.ScheduleSearch>> getAllSchedule(
            @PathVariable("userId") Long userId,
            @CurrentUser User currentUser
    ) {
        if (Objects.isNull(currentUser) || userId != currentUser.getUserId()) {
            return SuccessResponse.of(scheduleService.searchByUserId(userId));
        }else{
            return SuccessResponse.of(scheduleService.searchByMyId(userId));
        }
    }
}
