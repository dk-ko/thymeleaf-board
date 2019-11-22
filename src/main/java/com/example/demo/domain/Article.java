package com.example.demo.domain;

import com.example.demo.common.annotation.IPFormat;
import com.example.demo.common.utils.IPFormatUtils;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.ArticleResDto;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
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
    @IPFormat
    private String createdIP;

    @Column(length = 16)
    @IPFormat
    private String lastUpdatedIp;

    @Column(nullable = false, length = 20)
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_user"),
                nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_article_board"),
                nullable = false)
    private Board board;

    @OneToMany(mappedBy = "article",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    @IPFormat
    public Article(String title, String contents, Integer readCnt, Integer recommendCnt,
                   String createdIP, String lastUpdatedIp, String userName, User user, Board board, List<Comment> commentList) {
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

    public void editTitle(String title) {
        Assert.notNull(title, "title must be provided.");
        this.title = title;
    }

    public void editContents(String contents) {
        Assert.notNull(contents, "contents must be provided.");
        this.contents = contents;
    }

    public void editUpdatedIp(String updatedIp) {
        IPFormatUtils.checkIP(updatedIp, "updatedIp value is invalid");
        this.lastUpdatedIp = updatedIp;
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
                .userName(this.userName)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .commentList(this.commentList)
                .build();
    }

    public ArticleListResDto toListResDto() {
        return ArticleListResDto.builder()
                .articleIdx(this.idx)
                .title(this.title)
                .readCnt(this.readCnt)
                .userName(this.userName)
                .createdDate(this.createdDate)
                .numberOfComments(this.commentList.size())
                .board(this.board)
                .build();
    }
}
