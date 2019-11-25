package com.example.demo.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class CommentResDto {
    private Long idx;
    private LocalDateTime updatedDate;
    private String contents;
    private UserResDto userResDto;
    private ArticleResDto articleResDto;
}
