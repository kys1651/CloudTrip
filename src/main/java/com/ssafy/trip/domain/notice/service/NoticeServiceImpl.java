package com.ssafy.trip.domain.notice.service;

import com.ssafy.trip.core.service.S3UploadService;
import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.notice.mapper.NoticeMapper;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);
    private final NoticeMapper noticeMapper;
    private final S3UploadService s3UploadService;


    @Override
    public List<Notice> findNotices() {
        return noticeMapper.findNotices();
    }

    @Override
    public Optional<Notice> findNoticeById(Long noticeId) {
        return noticeMapper.findNoticeById(noticeId);
    }

    @Override
    public List<Notice> findNoticesByKeyword(String keyword) {
        return noticeMapper.findNoticesByKeyword(keyword);
    }

    @Override
    public void save(Notice notice, List<MultipartFile> images) {
        HashMap<String, String> imgUrlMap = new HashMap<>();
        // S3로 이미지 업로드
        if (images != null && images.size() > 0) {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            images.stream().map((image) -> {
                CompletableFuture<String> future = s3UploadService.upload(image);
                return future.join();
            }).forEach((imgUrl) -> {
                imgUrlMap.put("image-replace-key-" + atomicInteger.incrementAndGet(), imgUrl);
            });

            // notice.content 에서 image-replace-key를 찾아내면 대체
            String content = notice.getContent();
            for (Map.Entry<String, String> entry : imgUrlMap.entrySet()) {
                content = content.replace(entry.getKey(), entry.getValue());
            }
            notice.setContent(content);
        }

        noticeMapper.save(notice);

    }

    @Override
    public void update(Notice notice, List<MultipartFile> images,  Long noticeId) {
        HashMap<String, String> imgUrlMap = new HashMap<>();
        // S3로 이미지 업로드
        if (images != null && images.size() > 0) {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            images.stream().map((image) -> {
                CompletableFuture<String> future = s3UploadService.upload(image);
                return future.join();
            }).forEach((imgUrl) -> {
                imgUrlMap.put("image-replace-key-" + atomicInteger.incrementAndGet(), imgUrl);
            });

            // notice.content 에서 image-replace-key를 찾아내면 대체
            String content = notice.getContent();
            for (Map.Entry<String, String> entry : imgUrlMap.entrySet()) {
                content = content.replace(entry.getKey(), entry.getValue());
            }
            notice.setContent(content);
        }

        noticeMapper.update(notice, noticeId);
    }

    @Override
    public void delete(Long noticeId) {
        noticeMapper.delete(noticeId);
    }
}
