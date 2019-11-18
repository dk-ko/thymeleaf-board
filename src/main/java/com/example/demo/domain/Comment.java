package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Comment extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_user"),
                nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_article"),
                nullable = false)
    private Article article;

    @Builder
    public Comment(String contents, User user, Article article) {
        this.contents = contents;
        this.user = user;
        this.article = article;
    }
}
