package com.ssafy.trip.domain.user.service;

import com.ssafy.trip.core.service.S3UploadService;
import com.ssafy.trip.domain.review.entity.Comment.SimpleComment;
import com.ssafy.trip.domain.user.dto.UserData;
import com.ssafy.trip.domain.user.dto.UserData.Password;
import com.ssafy.trip.domain.user.dto.UserData.Update;
import com.ssafy.trip.domain.user.entity.SimpleUser;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.ssafy.trip.domain.user.dto.UserData.Profile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final S3UploadService s3UploadService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void update(Update update, MultipartFile profileImage, User user) {
        String originSrc = user.getProfileImage();
        String imageSrc = originSrc;
        if (Objects.nonNull(profileImage)) {
            CompletableFuture<String> future = s3UploadService.upload(profileImage);
            imageSrc = future.join();
        }
        if (update.isDefaultImage()) {
            imageSrc = User.DEFAULT_IMAGE;
        }
        if (!originSrc.equals(imageSrc) && imageSrc.equals(User.DEFAULT_IMAGE)) {
            s3UploadService.deleteImageFromS3(originSrc);
        }
        user.updateProfile(update.getNickname(), update.getCityCode(), update.getTownCode(), imageSrc);
        userMapper.update(user);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public Profile getProfile(Long userId) {
//        User user = userMapper.findById(userId);
//        return Profile.of(user);
        return null;
    }

    @Override
    public List<User> findByKeyword(String keyword) {
        return userMapper.findByKeyword(keyword);
    }

    @Override
    public void updateIsLocked(Long userId) {
        userMapper.updateIsLocked(userId);
    }

    @Override
    public void updatePassword(Password password, User user) {
        user.resetPassword(passwordEncoder.encode(password.getPassword()));
        userMapper.update(user);
    }

    @Override
    public void delete(User user) {
        String profileImagesSrc = userMapper.getProfileImage(user.getUserId());
        if (Objects.nonNull(profileImagesSrc) && !profileImagesSrc.equals(User.DEFAULT_IMAGE)) {
            s3UploadService.deleteImageFromS3(profileImagesSrc);
        }
        userMapper.delete(user.getUserId());
    }

    @Override
    public List<SimpleComment> commentsByUserId(Long userId) {
        return userMapper.commentsByUserId(userId);
    }

    @Override
    public List<UserData.SimpleReview> getReviewsById(Long userId) {
        return userMapper.getReviewsById(userId);
    }

    @Override
    public List<UserData.SimpleReview> getLikedReviewsById(Long userId) {
        return userMapper.getLikedReviewsById(userId);
    }

    @Override
    public UserData.SimpleProfile findById(Long userId, Long currentId) {
        return userMapper.findById(userId, currentId);
    }

    @Override
    public boolean isFollow(Long followeeId, Long followerId) {
        return userMapper.isFollow(followeeId, followerId);
    }

    @Override
    public void followUser(Long followeeId, Long followerId) {
        userMapper.follow(followeeId, followerId);
    }

    @Override
    public void unFollowUser(Long followeeId, Long followerId) {
        userMapper.unFollow(followeeId, followerId);
    }

    @Override
    public List<SimpleUser> getFollowing(Long userId, Long currentId) {
        return userMapper.getFollowing(userId, currentId);
    }

    @Override
    public List<SimpleUser> getFollowers(Long userId, Long currentId) {
        return userMapper.getFollowers(userId, currentId);
    }

    @Override
    public int getFollowerCount(Long userId) {
        return userMapper.getFollowerCount(userId);
    }

    @Override
    public int getFollowingCount(Long userId) {
        return userMapper.getFollowingCount(userId);
    }

    @Override
    public void drop(Long userId) {
        userMapper.delete(userId);
    }

}
