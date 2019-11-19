package com.example.demo.dto.req;

import lombok.Getter;

@Getter
public class ArticleUpdateReqDto {
    private String title;
    private String contents;
    private String updatedIp;
}
