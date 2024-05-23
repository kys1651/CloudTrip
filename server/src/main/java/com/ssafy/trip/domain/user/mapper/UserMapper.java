package com.ssafy.trip.domain.user.mapper;

import com.ssafy.trip.domain.review.dto.ReviewData;
import com.ssafy.trip.domain.review.dto.ReviewData.SimpleReview;
import com.ssafy.trip.domain.review.entity.Comment;
import com.ssafy.trip.domain.review.entity.Comment.SimpleComment;
import com.ssafy.trip.domain.review.entity.ReviewWithUser;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.dto.UserData.Update;
import com.ssafy.trip.domain.user.entity.SimpleUser;
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

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    void save(User user);

    void update(User user);

    void updateIsLocked(Long userId);

    void delete(Long userId);

    void deleteProfileImage(Long userId);

    String getProfileImage(Long userId);


    List<SimpleComment> commentsByUserId(Long userId);

    List<UserData.SimpleReview> getReviewsById(Long userId);

    List<UserData.SimpleReview> getLikedReviewsById(Long userId);

    UserData.SimpleProfile findById(Long userId,Long currentId);

    boolean isFollow(Long followeeId, Long followerId);

    void follow(Long followeeId, Long followerId);

    void unFollow(Long followeeId, Long followerId);

    List<SimpleUser> getFollowing(Long userId, Long currentId);

    List<SimpleUser> getFollowers(Long userId, Long currentId);

    int getFollowerCount(Long userId);

    int getFollowingCount(Long userId);
}
