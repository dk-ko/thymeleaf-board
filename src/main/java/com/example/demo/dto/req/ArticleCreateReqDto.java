package com.example.demo.dto.req;

import lombok.Getter;

@Getter
public class ArticleCreateReqDto {
    private String title;
    private String contents;
    private String createdIP;
}
