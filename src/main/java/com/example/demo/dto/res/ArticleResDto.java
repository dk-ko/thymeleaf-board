package com.example.demo.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ArticleResDto {
    private Long articleIdx;
    private String title;
    private String contents;
    private Integer readCnt;
    private Integer recommendCnt;
    private String createdIP;
    private String lastUpdatedIp;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private UserResDto userResDto;
    private List<CommentResDto> commentResDtoList;
    private BoardResDto boardResDto;
}