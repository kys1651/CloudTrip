package com.ssafy.trip.domain.user.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.auth.service.AuthService;
import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.review.dto.ReviewData.SimpleReview;
import com.ssafy.trip.domain.review.entity.Comment.SimpleComment;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.dto.UserData.Password;
import com.ssafy.trip.domain.user.dto.UserData.Profile;
import com.ssafy.trip.domain.user.dto.UserData.Update;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<UserData.LoginUser> me(@CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return SuccessResponse.of(UserData.LoginUser.authenticated(user));
    }

    @GetMapping("/{userId}")
    public SuccessResponse<Profile> getUser(@PathVariable Long userId) {
        Profile profile = userService.getProfile(userId);
        return SuccessResponse.of(profile);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 특정 회원 조회
    @GetMapping("")
    public ResponseEntity<List<User>> getUserByKeyword(@RequestParam String keyword) {
        List<User> list = userService.findByKeyword(keyword);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<Void> updateIsLocked(@PathVariable Long userId) {
        userService.updateIsLocked(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/me")
    public SuccessResponse<Void> update(
            Update update,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @CurrentUser User user
    ) {
        userService.update(update, profileImage, user);
        return SuccessResponse.empty();
    }

    @PatchMapping("/updatePassword")
    public SuccessResponse<Void> updatePassword(
            @RequestBody Password password,
            @CurrentUser User user
    ) {
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

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> dropUser(@PathVariable Long userId) {
        userService.drop(userId);
        return new ResponseEntity<>(HttpStatus.OK);
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
}
