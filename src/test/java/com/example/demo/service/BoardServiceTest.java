package com.example.demo.service;

import com.example.demo.common.IntegrationTest;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Slf4j
public class BoardServiceTest extends IntegrationTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    private static final String CREATE_BOARD_NAME = "board";
    private User user;
    private Board board;

    @Before
    public void setUp() {
        user = User.builder()
                .account("userId")
                .password("password")
                .name("test user")
                .build();
        userRepository.save(user);
    }

    @Test
    public void getBoardList() {
        // given
        final int NUMBER_OF_BOARDS = 5;
        IntStream.rangeClosed(1, NUMBER_OF_BOARDS).forEach(this::generatedBoardList);

        // when & then
        assertThat(boardService.getBoardList().size(), is(NUMBER_OF_BOARDS));
    }

    @Test
    public void getBoard() {
        // given
        Board board = generatedBoard();
        Board savedBoard = boardRepository.save(board);

        // when
        Board foundBoard = boardService.getBoard(savedBoard.getIdx());

        // then
        assertEquals(savedBoard.getName(), foundBoard.getName());
    }

    @Test
    public void editBoard() {
        // given
        Board board = generatedBoard();
        boardRepository.save(board);

        // when
        boardService.editBoard(board.getIdx(), "edit board name");
        log.info(board.toString());

        // then
        assertThat(CREATE_BOARD_NAME.equals(board.getName()), is(false));
    }

    @Test
    public void deleteBoard_관련article들삭제_user삭제X() {
        // given
        Board board = generatedBoard();
        boardRepository.save(board);

        final int NUMBER_OF_ARTICLES = 30;
        for (int i = 1; i <= NUMBER_OF_ARTICLES; i++) {
            board.getArticleList().add(this.generatedArticle(i, board));
        }

        log.info("board.getArticleList().size() : {}", board.getArticleList().size());
        assertEquals(NUMBER_OF_ARTICLES, board.getArticleList().size());

        // when
        List<Article> articleList = board.getArticleList();
        boardService.deleteBoard(board.getIdx());

        // then
        assertEquals(articleRepository.findById(articleList.get(0).getIdx()), Optional.empty());
        assertNotEquals(userRepository.findById(user.getIdx()), Optional.empty());
    }

    private Article generatedArticle(int index, Board board) {
        final String IP_VALID_FORMAT_VALUE = "127.0.0.1";
        Article article = Article.builder()
                .title("title" + index)
                .contents("contents")
                .createdIP(IP_VALID_FORMAT_VALUE)
                .lastUpdatedIp(IP_VALID_FORMAT_VALUE)
                .user(this.user)
                .userName(this.user.getName())
                .board(board)
                .build();
        return articleRepository.save(article);
    }

    private Board generatedBoard() {
        return Board.builder()
                .name(CREATE_BOARD_NAME)
                .build();
    }

    private void generatedBoardList(int index) {
        Board board = generatedBoard();
        board.editBoardName(board.getName() + index);
        boardRepository.save(board);
    }
}