package com.example.demo.repository;

import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(value = "test")
@Slf4j
@Ignore
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    private User savedUser;
    private Article savedArticle;
    private Board savedBoard;

    @Before
    public void setUp() throws Exception {
        final String account = "testAccount";
        final String password = "testPassword";
        savedUser = userRepository.save(User.builder().account(account).password(password).build());
        savedBoard = boardRepository.save(Board.builder().name("자유게시판").build());
        savedArticle = articleRepository.save(
                Article.builder()
                        .title("제목")
                        .contents("내용")
                        .createdIP("127.0.0.1")
                        .user(savedUser)
                        .board(savedBoard)
                        .build());
    }

    // TODO 게시글 생성, 수정, 삭제, 페이징, repository에 기본적으로 구현된 것 외에 메소드 추가하는 경우 테스트 작성, 아직 보류
}