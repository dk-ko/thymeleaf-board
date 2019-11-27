package com.example.demo.dto.req;

import com.example.demo.common.annotation.IPFormat;
import com.example.demo.common.utils.IPFormatUtils;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ArticleUpdateReqDto {
    // TODO validation
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    @NotEmpty
    @IPFormat // not working // TODO add @valid to controller
    private String updatedIp;

    @Builder
    public ArticleUpdateReqDto(final String title, final String contents, final String updatedIp) {
        IPFormatUtils.checkIP(updatedIp, "updatedIp value is invalid");

        this.title = title;
        this.contents = contents;
        this.updatedIp = updatedIp;
    }
}
