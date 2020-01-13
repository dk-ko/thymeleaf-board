package com.example.demo.controller;

import com.example.demo.dto.req.BoardReqDto;
import com.example.demo.dto.res.BoardResDto;
import com.example.demo.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Api(value = "Board(게시판 종류)")
@RestController
@AllArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @ApiOperation("게시판 생성") // todo admin만 가능
    @PostMapping("/boards")
    public String createBoard(BoardReqDto boardReqDto, Model model) {
        log.info("======createdBoard======");
        // todo boardReqDto를 어떻게 받지?
        BoardResDto board = boardService.createBoard(boardReqDto);
        log.info("boardReqDto: {}", boardReqDto);
        return "redirect:boards/"+board.getBoardIdx(); // todo 생성한 게시판 목록으로 이동
    }

    @ApiOperation("게시판 삭제") // todo admin만 가능
    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(@PathVariable final Long boardId) {
        log.info("======deleteBoard======");
        boardService.deleteBoard(boardId);
        return "redirect:"; // todo 홈 화면으로 이동
    }

    @ApiOperation("게시판 수정")
    @PutMapping("/boards/{boardId}")
    public String editBoard(@PathVariable final Long boardId, BoardReqDto boardReqDto) {
        log.info("======editBoard======");
        boardService.editBoard(boardId, boardReqDto);
        return "redirect:"; // todo 수정한 게시판 화면으로 이동
    }
}
