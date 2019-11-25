package com.example.demo.controller;

import com.example.demo.common.IntegrationTest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class ArticleControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    private User user;
    private Board board;

    @Before
    public void setUp() {
        user = User.builder().account("testUserId").password("password").build();
        userRepository.save(user);
        board = Board.builder().name("자유게시판").build();
        boardRepository.save(board);
    }

    // TODO DTO 검증 테스트(validation 등)
    @Test
    void test() {

    }

    private void generateArticle(int index) {
        Article article = Article.builder()
                .title("title" + index)
                .contents("blah")
                .createdIP("127.0.0.1")
                .user(this.user)
                .board(this.board)
                .build();
        articleRepository.save(article);
    }
}