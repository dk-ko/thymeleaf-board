package com.example.demo.dto.res;

import com.example.demo.domain.Comment;
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
    private String userName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<Comment> commentList;
    // TODO board 정보가 필요한가?
}
