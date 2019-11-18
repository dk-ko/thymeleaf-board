package com.example.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Board extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "board",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY, // TODO EAGER ? > 우선 개발 진행해보고 수정
                orphanRemoval = true)
    List<Article> articleList = new ArrayList<>();

    @Builder
    public Board(String name, List<Article> articleList) {
        this.name = name;
        this.articleList = Optional.ofNullable(articleList).orElse(this.articleList);
    }
}