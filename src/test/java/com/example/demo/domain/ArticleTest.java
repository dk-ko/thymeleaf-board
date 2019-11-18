package com.example.demo.domain;

import javafx.scene.shape.Arc;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
@ActiveProfiles(value = "test")
public class ArticleTest {

    private Article article;

    @Before
    public void setUp() {
        article = Article.builder()
                .title("제목")
                .contents("내용")
                .createdIP("127.0.0.1")
                .user(new User())
                .board(new Board())
                .build();
    }

    @Test
    public void 초기값테스트() {
        assertThat(article.getReadCnt(), is(0));
        assertThat(article.getRecommendCnt(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void title_null_test() {
        Article titleArticle = Article.builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void ip_null_test() {
        Article ipArticle = Article.builder().build();
    }

    @Test(expected = IllegalStateException.class)
    public void incorrect_ip_test() {
        Article incorrectIpArticle = Article.builder()
                .title("title")
                .contents("contents")
                .createdIP("123").build();
    }

    @Test
    public void correct_ip_test() {
        Article correctIpArticle = Article.builder()
                .title("title")
                .contents("contents")
                .createdIP("127.0.0.1")
                .build();
    }
}