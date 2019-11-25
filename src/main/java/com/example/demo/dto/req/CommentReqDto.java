package com.example.demo.dto.req;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
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

    public Comment toEntity(final User user, final Article article) {
        return Comment.builder()
                .contents(this.contents)
                .user(user)
                .article(article)
                .build();
    }
}
