package com.example.demo.dto.res;

import com.example.demo.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ArticleListResDto {
    private Long articleIdx;
    private LocalDateTime createdDate;
    private String title;
    private Integer readCnt;
    private String userName;
    private Board board;
    private Integer numberOfComments;
    // TODO board dto로
}
