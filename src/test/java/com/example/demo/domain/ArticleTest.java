package com.example.demo.domain;

import com.example.demo.common.JpaTest;
import com.example.demo.erros.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArticleTest extends JpaTest {

  private Article article;

  @Before
  public void setUp() {
    article = Article.builder().title("제목").contents("내용").createdIP("127.0.0.1").user(new User()).board(new Board())
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

  @Test(expected = InvalidFormatException.class)
  public void incorrect_ip_test() {
    Article incorrectIpArticle = Article.builder().title("title").contents("contents").createdIP("123").build();
  }

  @Test
  public void correct_ip_test() {
    Article correctIpArticle = Article.builder().title("title").contents("contents").createdIP("127.0.0.1").build();
  }

  // TODO annotation
  @Test(expected = InvalidFormatException.class)
  public void annotation_test() {
    Article createArticle = Article.builder().title("title").contents("contents").createdIP("0").build();
  }
}