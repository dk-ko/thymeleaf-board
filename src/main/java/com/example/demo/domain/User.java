package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
public class User extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 20)
    private String account;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 300)
    private String password;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    List<Article> articleList = new ArrayList<>();

    @Builder
    public User(String account, String name, String password, List<Comment> commentList, List<Article> articleList) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.commentList = Optional.ofNullable(commentList).orElse(this.commentList);
        this.articleList = Optional.ofNullable(articleList).orElse(this.articleList);
    }

    protected void setPassword(String password) {
        this.password = password;
    }
}
