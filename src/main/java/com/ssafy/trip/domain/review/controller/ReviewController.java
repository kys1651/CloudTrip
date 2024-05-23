package com.ssafy.trip.domain.review.controller;

import com.ssafy.trip.core.annotation.CurrentUser;
import com.ssafy.trip.core.entity.CustomPage;
import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.review.dto.ReviewData.Create;
import com.ssafy.trip.domain.review.dto.ReviewData.SimpleReview;
import com.ssafy.trip.domain.review.dto.ReviewData.Update;
import com.ssafy.trip.domain.review.service.ReviewService;
import com.ssafy.trip.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 목록 조회
     *
     * @param pageable {@link Pageable} 페이지 정보
     * @param user     {@link User} 현재 사용자
     * @return {@link SimpleReview} 리뷰 목록
     */
    @GetMapping("")
    public SuccessResponse<CustomPage<SimpleReview>> getReviews(
            @PageableDefault(size = 12) Pageable pageable,
            @CurrentUser User user
    ) {
        Long userId = user == null ? 0 : user.getUserId();
        return SuccessResponse.of(reviewService.getReviews(pageable, userId));
    }

    @GetMapping("/all")
    public SuccessResponse<List<SimpleReview>> getAllReview() {
        return SuccessResponse.of(reviewService.getAllReview());
    }

    @GetMapping("/search")
    public SuccessResponse<List<SimpleReview>> searchReview(@RequestParam String keyword) {
        return SuccessResponse.of(reviewService.searchReview(keyword));
    }

    /**
     * 리뷰 상세 조회
     *
     * @param reviewId 리뷰 ID
     * @param user     {@link User} 현재 사용자
     * @return {@link ReviewData.Review} 리뷰 상세 정보
     */
    @GetMapping("/{id}")
    public SuccessResponse<ReviewData.Review> getReview(
            @PathVariable("id") Long reviewId,
            @CurrentUser User user
    ) {
        Long userId = user == null ? 0 : user.getUserId();
        ReviewData.Review review = reviewService.findById(reviewId, userId).orElse(null);
        log.debug("review: {}", review);
        return SuccessResponse.of(review);
    }

    /**
     * 리뷰 생성
     *
     * @param create {@link Create} 리뷰 생성 정보
     * @param user   {@link User} 현재 사용자
     * @param images {@link MultipartFile} 이미지 파일
     * @return 응답
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> create(
            Create create,
            @CurrentUser User user,
            @RequestParam("images") List<MultipartFile> images
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.saveReview(create, user.getUserId(), images);
        return SuccessResponse.empty();
    }

    @PatchMapping("/{id}")
    public SuccessResponse<Void> update(
            Update update,
            @PathVariable("id") Long reviewId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "removeImages", required = false) List<String> removeImages,
            @CurrentUser User user
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.updateReview(user.getUserId(), reviewId, update, images, removeImages);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> delete(
            @CurrentUser User user,
            @PathVariable("id") Long id) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.deleteReview(id);
        return SuccessResponse.empty();
    }

    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> deleteAll(@RequestBody List<Integer> checkedList) {
        reviewService.deleteAllReview(checkedList);
        return SuccessResponse.empty();
    }

    @PostMapping("/{id}/like")
    public SuccessResponse<Void> like(@PathVariable("id") Long reviewId, @CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.saveLike(reviewId, user.getUserId());
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{id}/like")
    public SuccessResponse<Void> unLike(@PathVariable("id") Long reviewId, @CurrentUser User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.deleteLike(reviewId, user.getUserId());
        return SuccessResponse.empty();
    }

    /**
     * 리뷰 댓글 조회
     *
     * @param reviewId 리뷰 ID
     * @return {@link ReviewData.CommentResponse} 댓글 목록
     */
    @GetMapping("/{id}/comment")
    public SuccessResponse<List<ReviewData.CommentResponse>> getComments(@PathVariable("id") Long reviewId) {
        return SuccessResponse.of(reviewService.findByReviewId(reviewId));
    }

    /**
     * 리뷰 댓글 생성
     *
     * @param reviewId 리뷰 ID
     * @param create   {@link ReviewData.CommentCreate} 댓글 생성 정보
     * @param user     {@link User} 현재 사용자
     * @return 응답
     */
    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> createComment(
            @PathVariable("id") Long reviewId,
            @RequestBody ReviewData.CommentCreate create,
            @CurrentUser User user
    ) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.saveComment(reviewId, create, user.getUserId());
        return SuccessResponse.empty();
    }

    @DeleteMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> deleteComment(@CurrentUser User user, @PathVariable("id") Long commentId) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.deleteComment(commentId);
        return SuccessResponse.empty();
    }

    @PatchMapping("/{id}/comment")
    public SuccessResponse<Void> updateComment(
            @CurrentUser User user,
            @PathVariable("id") Long commentId, @RequestBody ReviewData.UpdateComment update) {
        if (user == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        reviewService.updateComment(commentId, update);
        return SuccessResponse.empty();
    }

    @GetMapping("/like/{userId}")
    public SuccessResponse<List<ReviewData.SimpleReview>> getLikedReviews(@PathVariable("userId") Long userId) {
        return SuccessResponse.of(reviewService.getLikedReviews(userId));
    }
}

