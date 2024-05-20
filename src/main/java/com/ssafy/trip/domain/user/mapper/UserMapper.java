package com.ssafy.trip.domain.user.mapper;

import com.ssafy.trip.domain.review.entity.Comment.SimpleComment;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    List<User> findAll();

    // 키워드로 회원 조회
    List<User> findByKeyword(String keyword);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    void save(User user);

    void update(User user);

    void updateIsLocked(Long userId);

    void delete(Long userId);

    List<SimpleComment> commentsByUserId(Long userId);

    List<UserData.SimpleReview> getReviewsById(Long userId);

    List<UserData.SimpleReview> getLikedReviewsById(Long userId);
}
