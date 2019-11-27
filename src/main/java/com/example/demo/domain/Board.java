package com.example.demo.domain;

import com.example.demo.dto.res.BoardResDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity implements Serializable {
    @Column(nullable = false, unique = true, length = 15)
    private String name;

    @Valid
    @OneToMany(mappedBy = "board",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    private List<Article> articleList = new ArrayList<>();

    @Builder
    public Board(final String name, final List<Article> articleList) {
        this.name = name;
        this.articleList = Optional.ofNullable(articleList).orElse(this.articleList);
    }

    public void editBoardName(final String name) {
        this.name = name;
    }

    public void changeArticle(final Article article) {
        this.articleList.add(article);
        article.changeBoard(this);
    }

    @Override
    public String toString() {
        return "Board{idx=" + this.idx +
                ", createdDate=" + this.createdDate +
                ", updatedDate=" + this.updatedDate +
                ", name=" + this.name +
                ", articleList.size()=" + this.articleList.size() +
                "}";
    }

    public BoardResDto toResDto() {
        return BoardResDto.builder()
                .boardIdx(this.idx)
                .name(this.name)
                .build();
    }
}