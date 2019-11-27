package com.example.demo.dto.req;

import com.example.demo.common.annotation.IPFormat;
import com.example.demo.common.utils.IPFormatUtils;
import com.example.demo.domain.Article;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ArticleCreateReqDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotEmpty
    @IPFormat // not working // TODO add @valid to controller
    private String createdIP;

    @Builder
    public ArticleCreateReqDto(final String title, final String contents, final String createdIP) {
        IPFormatUtils.checkIP(createdIP, "IP value is invalid");

        this.title = title;
        this.contents = contents;
        this.createdIP = createdIP;
    }

    public Article toEntity() {
        return Article.builder()
                .title(this.title)
                .contents(this.contents)
                .createdIP(this.createdIP)
                .lastUpdatedIp(this.createdIP)
                .build();
    }
}
