package com.example.demo.dto.req;

import com.example.demo.domain.Comment;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentReqDto {
    @NotEmpty
    private String contents;

    @Builder
    public CommentReqDto(final String contents) {
        this.contents = contents;
    }

    public Comment toEntity() {
        return Comment.builder()
                .contents(this.contents)
                .build();
    }
}
