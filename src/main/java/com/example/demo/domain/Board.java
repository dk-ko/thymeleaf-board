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
    @Column(nullable = false, unique = true, length = 15)
    private String name;

    @OneToMany(mappedBy = "board",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    private List<Article> articleList = new ArrayList<>();

    @Builder
    public Board(String name, List<Article> articleList) {
        this.name = name;
        this.articleList = Optional.ofNullable(articleList).orElse(this.articleList);
    }

    public void editBoardName(String name) {
        this.name = name;
    }
}