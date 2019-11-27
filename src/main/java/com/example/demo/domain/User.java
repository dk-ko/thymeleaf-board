package com.example.demo.domain;

import com.example.demo.dto.res.UserResDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 20)
    private String account;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 300)
    private String password;

    @Valid
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @Valid
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private List<Article> articleList = new ArrayList<>();

    @Builder
    public User(final String account, final String name, final String password, final List<Comment> commentList, final List<Article> articleList) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.commentList = Optional.ofNullable(commentList).orElse(this.commentList);
        this.articleList = Optional.ofNullable(articleList).orElse(this.articleList);
    }

    protected void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) return false;
        if (!this.getClass().equals(o.getClass())) return false;

        User inputUser = (User) o;
        return this.getIdx().equals(inputUser.getIdx());
    }

    @Override
    public String toString() {
        return "User{idx=" + this.idx +
                ", createdDate=" + this.createdDate +
                ", updatedDate=" + this.updatedDate +
                ", account=" + this.account +
                ", name=" + this.name +
                ", password=" + this.password +
                ", commentList.size()=" + this.commentList.size() +
                ", articleList.size()=" + this.articleList.size() +
                "}";
    }

    public void changeArticle(final Article article) {
        this.articleList.add(article);
        article.changeUser(this);
    }

    public void changeComment(final Comment comment) {
        this.commentList.add(comment);
        comment.changeUser(this);
    }

    public UserResDto toResDto() {
        return UserResDto.builder()
                .idx(this.idx)
                .name(this.name)
                .build();
    }
}
