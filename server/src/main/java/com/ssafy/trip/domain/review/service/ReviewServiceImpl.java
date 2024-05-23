package com.ssafy.trip.domain.review.service;

import com.ssafy.trip.core.entity.CustomPage;
import com.ssafy.trip.core.service.S3UploadService;
import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.review.dto.ReviewData.Create;
import com.ssafy.trip.domain.review.dto.ReviewData.SimpleReview;
import com.ssafy.trip.domain.review.dto.ReviewData.Update;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.review.mapper.ReviewCommentMapper;
import com.ssafy.trip.domain.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewCommentMapper reviewCommentMapper;
    private final S3UploadService s3UploadService;

    @Override
    public void deleteAllReview(List<Integer> checkedList) {
        checkedList.forEach(reviewId -> deleteReview(reviewId));
    }

    @Override
    public CustomPage<SimpleReview> getReviews(Pageable pageable, Long userId) {
        List<Long> pagingIds = reviewMapper.findPagingIds(pageable);
        List<ReviewWithUser> reviews = reviewMapper.findReviews(pagingIds, userId);
        int count = reviewMapper.countReviews();
        Page<SimpleReview> pageReviews = new PageImpl<>(reviews.stream().map(SimpleReview::of).toList(), pageable, count);
        return CustomPage.of(pageReviews.getContent(), pageReviews.getNumber(), !pageReviews.isLast());
    }

    @Override
    public List<SimpleReview> getAllReview() {
        List<ReviewWithUser> allReview = reviewMapper.getAllReview();
        return allReview.stream().map(SimpleReview::of).collect(Collectors.toList());
    }

    @Override
    public List<SimpleReview> searchReview(String keyword) {
        return reviewMapper.searchReview(keyword);
    }

    @Override
    public Optional<ReviewData.Review> findById(Long reviewId, Long userId) {
        return reviewMapper.findById(reviewId, userId).map(ReviewData.Review::of);
    }

    @Override
    public List<ReviewData.CommentResponse> findByReviewId(Long reviewId) {
        return reviewCommentMapper.findByReviewId(reviewId).stream().map(ReviewData.CommentResponse::of).toList();
    }

    @Override
    public List<SimpleReview> getLikedReviews(Long userId) {
        List<ReviewWithUser> LikedAllById = reviewMapper.findLikedAllById(userId);
        return LikedAllById.stream().map(SimpleReview::of).collect(Collectors.toList());
    }

    @Override
    public void updateReview(Long userId,Long reviewId, Update update, List<MultipartFile> images, List<String> removeImages) {
        // 사용하지 않는 기존 이미지 제거
        if(Objects.nonNull(removeImages) && !removeImages.isEmpty()) {
            removeImages.forEach(imgUrl -> {
                reviewMapper.deleteImg(reviewId,imgUrl);// db
                s3UploadService.deleteImageFromS3(imgUrl);// s3
            });
        }


        // 새로 들어온 이미지 추가
        if(Objects.nonNull(images) && !images.isEmpty()){
            images.stream().map((image) -> s3UploadService.upload(image).join())
                    .forEach((imgUrl) -> reviewMapper.saveImg(reviewId, imgUrl));
        }

        // 정보 업데이트
        reviewMapper.updateReview(reviewId, update);
    }

    @Override
    public void saveComment(Long reviewId, ReviewData.CommentCreate create, Long userId) {
        reviewCommentMapper.save(reviewId, create.getContent(), userId);
    }

    @Override
    public void deleteComment(Long commentId) {
        reviewCommentMapper.delete(commentId);
    }

    @Override
    public void updateComment(Long commentId, ReviewData.UpdateComment update) {
        reviewCommentMapper.update(commentId, update);
    }

    @Override
    @Transactional
    public void saveReview(Create create, Long userId, List<MultipartFile> images) {
        reviewMapper.saveReview(create, userId);
        images.stream().map((image) -> {
            CompletableFuture<String> future = s3UploadService.upload(image);
            return future.join();
        }).forEach((imgUrl) -> reviewMapper.saveImg(create.getReviewId(), imgUrl));
    }

    @Override
    @Transactional
    public void saveLike(long reviewId, long userId) {
        reviewMapper.saveLike(reviewId, userId);
        reviewMapper.updateLikeCount(reviewId, 1);
    }


    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        reviewMapper.getImg(reviewId).forEach(img -> s3UploadService.deleteImageFromS3(img));
        reviewMapper.deleteReview(reviewId);
    }

    @Override
    @Transactional
    public void deleteLike(long reviewId, long userId) {
        reviewMapper.deleteLike(reviewId, userId);
        reviewMapper.updateLikeCount(reviewId, -1);
    }

}
