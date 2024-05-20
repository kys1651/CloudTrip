package com.ssafy.trip.domain.notice.entity;

import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
public class Notice {
    private Long noticeId;
    private String title;
    private String content;
    private Timestamp createdAt;
}
