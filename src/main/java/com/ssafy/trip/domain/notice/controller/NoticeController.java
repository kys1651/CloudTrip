package com.ssafy.trip.domain.notice.controller;

import com.ssafy.trip.core.response.SuccessResponse;
import com.ssafy.trip.domain.notice.entity.Notice;
import com.ssafy.trip.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private Logger log = LoggerFactory.getLogger(getClass());

    // 공지사항 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<Notice>> findNotices() {
        return new ResponseEntity<>(noticeService.findNotices(), HttpStatus.OK);
    }

    // 공지사항 단건 조회
    @GetMapping("/view/{noticeId}")
    public ResponseEntity<Optional<Notice>> findNoticeById(@PathVariable Long noticeId) {
        return new ResponseEntity<>(noticeService.findNoticeById(noticeId), HttpStatus.OK);
    }

    // 특정 공지사항 조회
    @GetMapping("")
    public ResponseEntity<List<Notice>> findNoticeByKeyword(@RequestParam String keyword) {
        List<Notice> list = noticeService.findNoticesByKeyword(keyword);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 공지사항 등록
    @PostMapping("/create")
    public ResponseEntity<Void> save(
            Notice noticeDto,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
        ) {
        System.out.println(noticeDto);
        System.out.println(images);
        
        noticeService.save(noticeDto, images);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 공지사항 수정
    @PutMapping("/modify/{noticeId}")
    public ResponseEntity<Void> modify(@RequestBody Notice noticeDto, @PathVariable Long noticeId) {
        noticeService.update(noticeDto, noticeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 공지사항 삭제
    @DeleteMapping("/delete/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
