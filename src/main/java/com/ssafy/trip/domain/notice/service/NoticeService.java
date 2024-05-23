package com.ssafy.trip.domain.notice.service;

import com.ssafy.trip.domain.notice.entity.Notice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface NoticeService {
    // 공지사항 전체 조회
    List<Notice> findNotices();

    // 공지사항 단건 조회
    Optional<Notice> findNoticeById(Long noticeId);

    // 특정 공지사항 조회
    List<Notice> findNoticesByKeyword(String keyword);

    // 공지사항 등록
    void save(Notice notice, List<MultipartFile> images);

    // 공지사항 수정
    void update(Notice notice, List<MultipartFile> images, Long noticeId);

    // 공지사항 삭제
    void delete(Long noticeId);
}
