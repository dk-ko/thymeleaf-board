package com.example.demo.domain;

import com.example.demo.common.annotation.IPFormat;
import com.example.demo.common.utils.IPFormatUtils;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.ArticleResDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity implements Serializable {
    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    @Lob
    private String contents;

    @Column(nullable = false)
    private Integer readCnt = 0;

    @Column(nullable = false)
    private Integer recommendCnt = 0;

    // TODO annotation
    @Column(nullable = false, length = 16)
//    @IPFormat
    private String createdIP;

    @Column(length = 16)
//    @IPFormat
    private String lastUpdatedIp;

    @Column(nullable = false, length = 20)
    private String userName;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_user"),
                nullable = false)
    private User user;

    @Valid
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_board"),
                nullable = false)
    private Board board;

    @Valid
    @OneToMany(mappedBy = "article",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Article(final String title, final String contents, final Integer readCnt, final Integer recommendCnt,
                   final String createdIP, final String lastUpdatedIp, final String userName, final User user, final Board board, final List<Comment> commentList) {
        Assert.notNull(title, "title must be provided.");
        Assert.notNull(contents, "contents must be provided.");
        IPFormatUtils.checkIP(createdIP, "createdIP value is invalid");

        this.title = title;
        this.contents = contents;
        this.readCnt = Optional.ofNullable(readCnt).orElse(this.readCnt);
        this.recommendCnt = Optional.ofNullable(recommendCnt).orElse(this.recommendCnt);
        this.createdIP = createdIP;
        this.lastUpdatedIp = lastUpdatedIp;
        this.userName = userName;
        this.user = user;
        this.board = board;
        this.commentList = Optional.ofNullable(commentList).orElse(this.commentList);
    }

    public void editTitle(final String title) {
        Assert.notNull(title, "title must be provided.");
        this.title = title;
    }

    public void editContents(final String contents) {
        Assert.notNull(contents, "contents must be provided.");
        this.contents = contents;
    }

    public void editUpdatedIp(final String updatedIp) {
        IPFormatUtils.checkIP(updatedIp, "updatedIp value is invalid");
        this.lastUpdatedIp = updatedIp;
    }

    public void addReadCnt() {
        this.readCnt++;
    }

    public void addRecommendCnt() {
        this.recommendCnt++;
    }

    public ArticleResDto toResDto() {
        return ArticleResDto.builder()
                .articleIdx(this.idx)
                .title(this.title)
                .contents(this.contents)
                .readCnt(this.readCnt)
                .recommendCnt(this.recommendCnt)
                .createdIP(this.createdIP)
                .lastUpdatedIp(this.lastUpdatedIp)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .userResDto(this.user.toResDto())
                .commentResDtoList(this.commentList.stream().map(Comment::toResDto).collect(Collectors.toList()))
                .boardResDto(this.board.toResDto())
                .build();
    }

    public ArticleListResDto toListResDto() {
        return ArticleListResDto.builder()
                .articleIdx(this.idx)
                .title(this.title)
                .readCnt(this.readCnt)
                .userResDto(this.user.toResDto())
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .numberOfComments(this.commentList.size())
                .boardResDto(this.board.toResDto())
                .build();
    }

    @Override
    public String toString() {
        return "Article{idx=" + this.idx +
                ", createdDate=" + this.createdDate +
                ", updatedDate=" + this.updatedDate +
                ", title=" + this.title +
                ", contents=" + this.contents +
                ", readCnt=" + this.readCnt +
                ", recommendCnt=" + this.recommendCnt +
                ", createdIp=" + this.createdIP +
                ", lastUpdatedIp=" + this.lastUpdatedIp +
                ", userName=" + this.userName +
                ", User{idx=" + this.user.getIdx() +
                "}, Board{idx=" + this.board.getIdx() +
                "}, CommentList.size()=" + this.commentList.size() +
                "}";
    }

    public void changeUser(final User user) {
        this.user = user;
        this.userName = user.getName();
        user.getArticleList().add(this);
    }

    public void changeBoard(final Board board) {
        this.board = board;
        board.getArticleList().add(this);
    }

    public void changeComment(final Comment comment) {
        this.commentList.add(comment);
        comment.changeArticle(this);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (!this.getClass().equals(obj.getClass())) return false;

        Article inputArticle = (Article) obj;
        return this.getIdx().equals(inputArticle.getIdx());
    }
}
