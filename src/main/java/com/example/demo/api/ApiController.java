package com.example.demo.api;

import com.example.demo.common.model.PageRequest;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.dto.res.BoardResDto;
import com.example.demo.dto.res.CommentResDto;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
@Api("Api")
@Slf4j
public class ApiController {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final BoardService boardService;

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // todo boards controller 완성 후 지우기
    }

    @ApiOperation("게시판의 게시글 목록 뷰")
    @GetMapping("/boards/{boardIdx}")
    public String getArticlesByBoard(@PathVariable final Long boardIdx, PageRequest pageRequest, Model model) {
        log.info("======BoardController.getArticlesByBoard======");

        List<BoardResDto> boardList = boardService.getBoardList(); // todo 캐싱?
        log.info("boardList: {}", boardList);
        model.addAttribute("boardList", boardList);

        log.info("pageRequest: {}", pageRequest);
        Page<ArticleListResDto> articlesByPageable = articleService.getArticlesByPageable(boardIdx, pageRequest);
        log.info("articlesByPageable: {}", articlesByPageable);
        model.addAttribute("articleList", articlesByPageable);

        model.addAttribute("boardName", articlesByPageable.getContent().get(0).getBoardResDto().getName());
        return "board/list";
    }

    @ApiOperation("게시글 뷰(댓글 목록)")
    @GetMapping("/articles/{articleIdx}")
    public String getArticle(@PathVariable final Long articleIdx, PageRequest pageRequest, Model model) {
        ArticleResDto article = articleService.getArticle(articleIdx);
        model.addAttribute("article", article);

        model.addAttribute("boardName", article.getBoardResDto().getName());
        model.addAttribute("boardIdx", article.getBoardResDto().getBoardIdx());

        pageRequest.setSize(10);
        pageRequest.setDirection(Sort.Direction.ASC);
        Page<CommentResDto> comments = commentService.getComments(articleIdx, pageRequest);
        model.addAttribute("commentList", comments);
        return "article/contents";
    }

    @ApiOperation("게시판 생성/수정 폼") // todo admin만 가능
    @GetMapping("/boards")
    public String createBoardView() {
        return "/board/form";
    }

}
