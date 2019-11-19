package com.example.demo.domain;

import com.example.demo.dto.res.ArticleResDto;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Column(nullable = false, length = 16)
    private String createdIP;

    @Column(length = 16)
    private String updatedIp;

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
    public Article(String title, String contents, Integer readCnt, Integer recommendCnt,
                   String createdIP, String updatedIp, String userName, User user, Board board, List<Comment> commentList) {
        Assert.notNull(title, "title must be provided.");
        Assert.notNull(contents, "contents must be provided.");
        checkIP(createdIP, "createdIP value is invalid");

        this.title = title;
        this.contents = contents;
        this.readCnt = Optional.ofNullable(readCnt).orElse(this.readCnt);
        this.recommendCnt = Optional.ofNullable(recommendCnt).orElse(this.recommendCnt);
        this.createdIP = createdIP;
        this.updatedIp = updatedIp;
        this.userName = userName;
        this.user = user;
        this.board = board;
        this.commentList = Optional.ofNullable(commentList).orElse(this.commentList);
    }

    private void checkIP(String createdIP, String message) {
        if (createdIP == null) throw new IllegalStateException("createdIP must be provided.");

        final String IP_ADDRESS_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(createdIP);
        if (!matcher.matches()) throw new IllegalStateException(message);
    }

    public void editArticle(String title, String contents, String updatedIp) {
        Assert.notNull(title, "title must be provided.");
        Assert.notNull(contents, "contents must be provided.");
        checkIP(updatedIp, "updatedIp value is invalid");

        this.title = title;
        this.contents = contents;
        this.updatedIp = updatedIp;
    }

    public ArticleResDto toResDto() {
        return ArticleResDto.builder()
                .title(this.title)
                .contents(this.contents)
                .readCnt(this.readCnt)
                .recommendCnt(this.recommendCnt)
                .createdIP(this.createdIP)
                .updatedIp(this.updatedIp)
                .userName(this.userName)
                .createdDate(this.createdDate)
                .updatedDate(this.updatedDate)
                .commentList(this.commentList)
                .build();
    }
}
