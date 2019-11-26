package com.example.demo.service;

import com.example.demo.common.IntegrationTest;
import com.example.demo.domain.Board;
import com.example.demo.dto.req.BoardReqDto;
import com.example.demo.dto.res.BoardResDto;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        BoardResDto foundBoard = boardService.getBoard(savedBoard.getIdx());

        // then
        assertEquals(savedBoard.getName(), foundBoard.getName());
    }

    @Test
    public void editBoard() {
        // given
        Board board = generatedBoard();
        boardRepository.save(board);
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .name("edit board name").build();

        // when

        boardService.editBoard(board.getIdx(), boardReqDto);
        log.info(board.toString());

        // then
        assertThat(CREATE_BOARD_NAME.equals(board.getName()), is(false));
    }

    @Test
    public void deleteBoard() {
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