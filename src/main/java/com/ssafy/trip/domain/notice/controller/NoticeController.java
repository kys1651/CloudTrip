package com.ssafy.trip.domain.notice.controller;

import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private Logger log = LoggerFactory.getLogger(getClass());

    // 공지사항 전체 조회
    @GetMapping("")
    public SuccessResponse<List<Notice>> findNotices() {
        return SuccessResponse.of(noticeService.findNotices());
    }

    // 공지사항 단건 조회
    @GetMapping("/{noticeId}")
    public SuccessResponse<Optional<Notice>> findNoticeById(@PathVariable Long noticeId) {
        return SuccessResponse.of(noticeService.findNoticeById(noticeId));
    }

    // 특정 공지사항 조회
    @GetMapping("/search")
    public SuccessResponse<List<Notice>> findNoticeByKeyword(@RequestParam String keyword) {
        List<Notice> list = noticeService.findNoticesByKeyword(keyword);
        return SuccessResponse.of(list);
    }

    // 공지사항 등록
    @PostMapping("/create")
    public SuccessResponse<Void> save(
            Notice noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
        ) {
        System.out.println(noticeDto);
        System.out.println(images);
        
        noticeService.save(noticeDto, images);
        return SuccessResponse.empty();
    }

    // 공지사항 수정
    @PutMapping("/{noticeId}")
    public SuccessResponse<Void> modify(Notice noticeDto, @RequestParam(value="images", required = false) List<MultipartFile> images, @PathVariable Long noticeId) {
        System.out.println(noticeDto);
        System.out.println(images);

        noticeService.update(noticeDto, images, noticeId);
        return SuccessResponse.empty();
    }

    // 공지사항 삭제
    @DeleteMapping("/{noticeId}")
    public SuccessResponse<Void> delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return SuccessResponse.empty();
    }

}
