package com.example.demo.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ArticleListResDto {
    private Long articleIdx;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String title;
    private Integer readCnt;
    private UserResDto userResDto;
    private BoardResDto boardResDto;
    private Integer numberOfComments;
}
