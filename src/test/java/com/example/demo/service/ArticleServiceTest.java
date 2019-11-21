package com.example.demo.service;

import com.example.demo.common.IntegrationTest;
import com.example.demo.common.model.PageRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class ArticleServiceTest extends IntegrationTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    private Board board;
    private User user;

    @Before
    public void setUp() {
        user = User.builder().account("testUserId").name("testUserName").password("password").build();
        userRepository.save(user);
        board = Board.builder().name("자유게시판").build();
        boardRepository.save(board);
    }

    @Test
    public void 페이징테스트() {
        IntStream.rangeClosed(1, 70).forEach(this::generateArticle);

        List<Article> articleList = articleRepository.findAll();
        log.info(articleList.toString());

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(5);
        pageRequest.setSize(15);
        pageRequest.setDirection(Sort.Direction.DESC);

        Page<Article> articles = articleService.getArticlesByPageable(this.board.getIdx(), pageRequest);
        log.info("articles.toString() : {}", articles.toString());
        log.info("articles.getContent().toString() : {}", articles.getContent().toString());
        log.info("articles.getTotalElements() : {}", articles.getTotalElements());
        log.info("articles.getTotalPages() : {}", articles.getTotalPages());
        log.info("articles.getNumberOfElements() : {}", articles.getNumberOfElements());
        log.info("articles.getNumber() : {}", articles.getNumber());
        log.info("articles.getPageable() : {}", articles.getPageable());

        assertThat(articles.getTotalPages(), is(5));
        assertThat(articles.getNumberOfElements(), is(10));
    }

    @Test
    public void 역순정렬테스트() {
        IntStream.rangeClosed(1, 10).forEach(this::generateArticle);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(15);
        pageRequest.setDirection(Sort.Direction.DESC);
        Page<Article> articles = articleService.getArticlesByPageable(board.getIdx(), pageRequest);
        assertThat(articles.getContent().get(0).getIdx(), is(10L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 없는페이지조회() {
        IntStream.rangeClosed(1, 70).forEach(this::generateArticle);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(7);
        pageRequest.setPage(15);
        pageRequest.setDirection(Sort.Direction.DESC);
        articleService.getArticlesByPageable(board.getIdx(), pageRequest);
    }

    private void generateArticle(int index) {
        Article article = Article.builder()
                .title("title" + index)
                .contents("blah")
                .createdIP("127.0.0.1")
                .userName(this.user.getAccount())
                .user(this.user)
                .board(this.board)
                .build();
        articleRepository.save(article);
    }
}