package com.example.demo.domain;

import com.example.demo.dto.res.CommentResDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity implements Serializable {

    @Column(nullable = false)
    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_user"),
                nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_article"),
                nullable = false)
    private Article article;

    @Builder
    public Comment(final String contents, final User user, final Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }

    public void changeArticle(final Article article) {
        this.article = article;
        article.getCommentList().add(this);
    }

    public void changeUser(final User user) {
        this.user = user;
        user.getCommentList().add(this);
    }

    @Override
    public String toString() {
        return "Comment{idx=" + this.idx +
                ", contents=" + this.contents +
                ", user{idx=" + this.getUser().getIdx() +
                "}, Article{idx=" + this.getArticle().getIdx() +
                "}}";
    }

    public CommentResDto toResDto() {
        return CommentResDto.builder()
                .idx(this.idx)
                .contents(this.contents)
                .userResDto(this.user.toResDto())
                .updatedDate(this.updatedDate)
                .articleResDto(this.article.toResDto())
                .build();
    }
}
