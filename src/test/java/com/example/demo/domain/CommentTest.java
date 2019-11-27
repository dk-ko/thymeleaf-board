package com.example.demo.domain;

import com.example.demo.common.JpaTest;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
public class CommentTest extends JpaTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Board board;
    private Article article;

    @Before
    public void setUp() {
        user = User.builder().account("id").name("name").password("pw").build();
        userRepository.save(user);
        board = Board.builder().name("free").build();
        boardRepository.save(board);
        article = Article.builder()
                .title("title")
                .contents("contents")
                .user(user)
                .userName(user.getName())
                .board(board)
                .createdIP("127.0.0.1")
                .lastUpdatedIp("127.0.0.1")
                .build();
        article.changeUser(user);
        article.changeBoard(board);
        articleRepository.save(article);
    }

    @Test
    public void comment연관관계_맵핑테스트() {
        Comment comment = Comment.builder()
                .contents("contents")
                .user(user)
                .article(article)
                .build();
        comment.changeArticle(article);
        comment.changeUser(user);
        // 연관관계 저장
        Comment savedComment = commentRepository.save(comment);
        // comment 저장

        // 저장한 comment 에서 article 꺼내기
        Article article = savedComment.getArticle();
        log.info("savedComment.getArticle() : {}", article);
        log.info("savedComment.getUser() : {}", savedComment.getUser());

        // 꺼낸 article id로 article 조회
        Optional<Article> foundArticle = articleRepository.findById(article.getIdx());
        // article에 comment 저장되었나 확인
        log.info("foundArticle.get().getCommentList().size() : {}", foundArticle.get().getCommentList().size());
    }

    @Test
    public void article연관관계_역방향_맵핑테스트() {
        Article article = Article.builder()
                .title("title")
                .contents("contents")
                .user(user)
                .userName(user.getName())
                .board(board)
                .createdIP("127.0.0.1")
                .lastUpdatedIp("127.0.0.1")
                .build();
        // user, board 저장 후 article 객체 생성,
        Comment comment = Comment.builder()
                .contents("contents")
                .user(user)
                .article(article)
                .build();
        // 연관관계 지정
        article.changeComment(comment);
        articleRepository.save(article);

        // comment와 article 연관관계 확인
        Optional<Comment> foundComment = commentRepository.findById(article.getCommentList().get(0).getIdx());
        log.info("foundComment.get().toString() : {}", foundComment.get().toString());
        assertNotEquals(foundComment.get(), Optional.empty());
    }
}