package com.example.demo.controller;

import com.example.demo.common.model.PageRequest;
import com.example.demo.dto.req.BoardReqDto;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.BoardResDto;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Board(게시판 종류)")
@Controller
@AllArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final ArticleService articleService;

    @ApiOperation("게시판 생성 뷰 이동") // todo admin만 가능
    @GetMapping("/boards")
    public String createBoardView() {
        return "/board/form";
    }

    // TODO 게시판 목록을 조회하는 api(ex boardList)를 만드는 것보다 게시판 목록 정보

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

    @ApiOperation("게시판의 게시글 목록 조회")
    @GetMapping("/boards/{boardIdx}")
    public String getArticlesByBoard(@PathVariable final Long boardIdx, @PageableDefault PageRequest pageRequest, Model model) {
        log.info("======BoardController.getArticlesByBoard======");

        List<BoardResDto> boardList = boardService.getBoardList(); // todo 캐싱?
        log.info("boardList: {}", boardList);
        model.addAttribute("boardList", boardList);

        // todo pagabledefault
        log.info("pageRequest: {}", pageRequest);
        Page<ArticleListResDto> articlesByPageable = articleService.getArticlesByPageable(boardIdx, pageRequest);
        log.info("articlesByPageable: {}", articlesByPageable);
        model.addAttribute("articleList", articlesByPageable);

        model.addAttribute("boardName", articlesByPageable.getContent().get(0).getBoardResDto().getName());
        // TODO 이렇게 처리 안하고 뷰에서 전달받은 boardList에서 꺼낼순 없는건가? 객체 접근이 안되나?
        return "board/list";
    }

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // todo boards controller 완성 후 지우기
    }
}
