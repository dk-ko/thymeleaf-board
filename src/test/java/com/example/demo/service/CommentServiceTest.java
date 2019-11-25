package com.example.demo.service;

import com.example.demo.common.IntegrationTest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.req.CommentReqDto;
import com.example.demo.dto.res.CommentResDto;
import com.example.demo.erros.UnauthorizedException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertNotEquals;

@Slf4j
public class CommentServiceTest extends IntegrationTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User writer;
    private User reader;
    private Board board;
    private Article article;

    @Before
    public void setUp() {
        writer = User.builder()
                .account("writer")
                .name("writer")
                .password("password")
                .build();
        userRepository.save(writer);

        reader = User.builder()
                .account("reader")
                .name("reader")
                .password("another")
                .build();
        userRepository.save(reader);

        board = Board.builder()
                .name("자유게시판")
                .build();
        boardRepository.save(board);

        article = Article.builder()
                .title("article")
                .contents("article contents")
                .createdIP("127.0.0.1")
                .lastUpdatedIp("127.0.0.1")
                .user(writer)
                .userName(writer.getName())
                .board(board)
                .build();
        articleRepository.save(article);
    }

    @Test
    public void comment생성테스트_연관관계조회() {
        // given
        CommentReqDto dto = CommentReqDto.builder()
                .contents("댓글 작성")
                .build();

        // when
        CommentResDto commentResDto = commentService.createComment(article.getIdx(), writer, dto);
        log.info("comment.toString() : {}", commentResDto.toString());

        // then
        Optional<User> foundUser = userRepository.findById(commentResDto.getUserResDto().getIdx());
        assertNotEquals(foundUser, Optional.empty());
        Optional<Article> foundArticle = articleRepository.findById(commentResDto.getArticleResDto().getArticleIdx());
        assertNotEquals(foundArticle, Optional.empty());
    }

    @Test(expected = UnauthorizedException.class)
    public void comment수정fail테스트_작성자본인아닌경우() {
        // given
        CommentReqDto createDto = CommentReqDto.builder()
                .contents("댓글 작성")
                .build();
        commentService.createComment(article.getIdx(), writer, createDto);

        // when & then
        CommentReqDto editDto = CommentReqDto.builder()
                .contents("댓글 수정")
                .build();
        commentService.editComment(article.getIdx(), reader, editDto);
    }

    @Test
    public void comment수정success테스트() {
        // given
        final String CREATED_CONTENTS = "댓글 작성";
        CommentReqDto createDto = CommentReqDto.builder()
                .contents(CREATED_CONTENTS)
                .build();
        commentService.createComment(article.getIdx(), writer, createDto);

        // when
        CommentReqDto editDto = CommentReqDto.builder()
                .contents("댓글 수정")
                .build();
        CommentResDto editComment = commentService.editComment(article.getIdx(), writer, editDto);

        // then
        assertNotEquals(editComment.getContents(), CREATED_CONTENTS);
    }
}