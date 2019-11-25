package com.example.demo.service;

import com.example.demo.common.IntegrationTest;
import com.example.demo.common.model.PageRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.dto.req.ArticleCreateReqDto;
import com.example.demo.dto.req.ArticleUpdateReqDto;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.erros.InvalidFormatException;
import com.example.demo.erros.UnauthorizedException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class ArticleServiceTest extends IntegrationTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

    private Board board;
    private User user;
    private static final String IP_VALID_FORMAT_VALUE = "127.0.0.1";

    @Before
    public void setUp() {
        user = User.builder().account("testUserId").name("testUserName").password("password").build();
        userRepository.save(user);
        board = Board.builder().name("자유게시판").build();
        boardRepository.save(board);
    }

    @Test
    public void 페이징테스트() {
        // given
        IntStream.rangeClosed(1, 70).forEach(this::generateArticleList);

        List<Article> articleList = articleRepository.findAll();
        log.info(articleList.toString());

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(5);
        pageRequest.setSize(15);
        pageRequest.setDirection(Sort.Direction.DESC);

        // when
        Page<ArticleListResDto> articles = articleService.getArticlesByPageable(this.board.getIdx(), pageRequest);
        log.info("articles.toString() : {}", articles.toString());
        log.info("articles.getContent().toString() : {}", articles.getContent().toString());
        log.info("articles.getTotalElements() : {}", articles.getTotalElements());
        log.info("articles.getTotalPages() : {}", articles.getTotalPages());
        log.info("articles.getNumberOfElements() : {}", articles.getNumberOfElements());
        log.info("articles.getNumber() : {}", articles.getNumber());
        log.info("articles.getPageable() : {}", articles.getPageable());

        // then
        assertThat(articles.getTotalPages(), is(5));
        assertThat(articles.getNumberOfElements(), is(10));
    }

    @Test
    public void a_reverseSort테스트() {
        // given
        IntStream.rangeClosed(1, 10).forEach(this::generateArticleList);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(15);

        // when
        Page<ArticleListResDto> articles = articleService.getArticlesByPageable(board.getIdx(), pageRequest);

        // then
        assertThat(articles.getContent().get(0).getArticleIdx(), is(10L));
    }

    @Test
    public void editArticleMethodSuccessTest() {
        // given
        final String CREATE_TITLE = "create";
        Article createdArticle = generateArticle();
        createdArticle.editTitle(CREATE_TITLE);

        articleRepository.save(createdArticle);
        log.info("createdArticle.getTitle(): {}", createdArticle.getTitle());
        assertEquals(createdArticle.getCreatedIP(), createdArticle.getLastUpdatedIp());

        // when
        ArticleUpdateReqDto updateReqDto = ArticleUpdateReqDto.builder()
                .title("update")
                .contents("update contents")
                .updatedIp("192.168.92.3")
                .build();
        articleService.editArticle(createdArticle.getIdx(), user, updateReqDto);
        log.info("createdArticle.getTitle(): {}", createdArticle.getTitle());

        // then
        assertNotEquals(CREATE_TITLE, createdArticle.getTitle());
        assertNotEquals(createdArticle.getCreatedIP(), createdArticle.getLastUpdatedIp());
    }

    @Test(expected = UnauthorizedException.class)
    public void editArticleMethodFail_유저불일치_Test() {
        // given
        Article thisUserArticle = generateArticle();
        articleRepository.save(thisUserArticle);

        ArticleUpdateReqDto updateReqDto = ArticleUpdateReqDto.builder()
                .title("edit title")
                .contents("edit contents")
                .updatedIp("192.0.0.1")
                .build();

        User newUser = User.builder()
                .name("newbie")
                .account("id")
                .password("passwd")
                .build();
        userRepository.save(newUser);

        // when & then
        articleService.editArticle(thisUserArticle.getIdx(), newUser, updateReqDto);
    }

    @Test
    public void deleteArticleMethod_연관댓글삭제_Test() {
        // given
        Article article = generateArticle();
        articleRepository.save(article);

        final int NUMBER_OF_COMMENTS = 10;
        for (int i = 1; i <= NUMBER_OF_COMMENTS; i++) {
            article.getCommentList().add(this.generateComments(i, article));
        }
        assertThat(article.getCommentList().size(), is(NUMBER_OF_COMMENTS));

        // when
        List<Comment> commentList = article.getCommentList();
        articleService.deleteArticle(article.getIdx(), user);

        // then
        assertThat(commentRepository.findById(commentList.get(0).getIdx()), is(Optional.empty()));
    }

    @Test
    public void article생성success_연관관계확인_테스트() {
        // given
        ArticleCreateReqDto dto = ArticleCreateReqDto.builder()
                .title("create article dto")
                .contents("create contents")
                .createdIP(IP_VALID_FORMAT_VALUE)
                .build();

        // when
        ArticleResDto createdArticle = articleService.createArticle(dto, user, board.getIdx());

        // then
        Optional<Article> foundArticle = articleRepository.findById(createdArticle.getArticleIdx());
        log.info("foundArticle.get() : {}", foundArticle.get());
        assertNotEquals(foundArticle, is(Optional.empty()));

        log.info("user.getArticleList() : {}", user.getArticleList());
        assertNotEquals(user.getArticleList().size(), 0);

        log.info("board.getArticleList() : {}", board.getArticleList());
        assertNotEquals(board.getArticleList().size(), 0);
    }

    @Test(expected = InvalidFormatException.class) // 객체를 사용해 직접 저장할 때 어노테이션이 동작함. (2)
    public void article생성fail테스트() {
        // given
        ArticleCreateReqDto dto = ArticleCreateReqDto.builder()
                .title("create article dto")
                .contents("create contents")
                .createdIP("invalid format")
                .build();

        // when
        ArticleResDto createdArticle = articleService.createArticle(dto, user, board.getIdx());

        // then
        assertNotEquals(articleRepository.findById(createdArticle.getArticleIdx()), is(Optional.empty()));
    }

    // TODO annotation 안쓰게되면 삭제할 것, "article생성fail테스트" 주석제거
    @Test(expected = InvalidFormatException.class) // 객체를 생성할 때는 체크를 못하고, (1)
    public void annotation_test() {
        Article createArticle = Article.builder()
                .title("title")
                .contents("contents")
                .createdIP("invalid format")
                .build();
    }

    @Test
    public void 조회수테스트() {
        // given
        Article article = generateArticle();
        articleRepository.save(article);
        log.info("article.getReadCnt() : {}", article.getReadCnt());
        assertThat(article.getReadCnt(), is(0));

        // when
        articleService.getArticle(article.getIdx());

        // then
        assertThat(article.getReadCnt() > 0, is(true));
    }

    @Test
    public void 추천수테스트() {
        // given
        Article article = generateArticle();
        articleRepository.save(article);
        log.info("article.getRecommendCnt() : {}", article.getRecommendCnt());

        // when
        articleService.addRecommendCnt(article.getIdx(), user);

        // then
        assertThat(article.getRecommendCnt() > 0, is(true));
    }

    private Article generateArticle() {
        return Article.builder()
                .title("title")
                .contents("contents")
                .createdIP(IP_VALID_FORMAT_VALUE)
                .lastUpdatedIp(IP_VALID_FORMAT_VALUE)
                .userName(this.user.getName())
                .user(this.user)
                .board(this.board)
                .build();
    }

    private void generateArticleList(int index) {
        Article article = generateArticle();
        article.editTitle(article.getTitle() + index);
        articleRepository.save(article);
    }

    private Comment generateComments(int index, Article article) {
        Comment comment = Comment.builder()
                .contents("comment" + index)
                .article(article)
                .user(this.user)
                .build();
        return commentRepository.save(comment);
    }
}