package com.ssafy.trip.domain.review.mapper;

import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.review.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewCommentMapper {
    void save(Long reviewId, String content, Long userId);

    void delete(Long commentId);

    void update(Long commentId, ReviewData.UpdateComment update);

    List<Comment> findByReviewId(Long reviewId);
}
