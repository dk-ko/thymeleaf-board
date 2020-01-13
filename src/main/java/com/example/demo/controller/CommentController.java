package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.req.CommentReqDto;
import com.example.demo.dto.res.CommentResDto;
import com.example.demo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "댓글")
@RestController
@AllArgsConstructor
@Slf4j
public class CommentController {
    private CommentService commentService;

    @ApiOperation("댓글 생성")
    @PostMapping("/articles/{articleIdx}")
    public CommentResDto createComment(@PathVariable final Long articleIdx, @RequestBody @Valid final CommentReqDto commentReqDto, Model model) {
        log.info("====CommentController.createComment====");
        log.info("params[articleIdx: {}, commentReqDto: {}]", articleIdx, commentReqDto);
        CommentResDto comment = commentService.createComment(articleIdx, User.builder().build(), commentReqDto); // todo login user

        model.addAttribute("comment", comment);
        log.info("model.addAttribute(\"comment\", comment);");
        log.info("comment: {}", comment);
        return comment;
    }

    @ApiOperation("댓글 수정")
    @PutMapping("/comments/{commentIdx}")
    public CommentResDto editComment(@PathVariable final Long commentIdx, @RequestBody @Valid final CommentReqDto commentReqDto, Model model) {
        log.info("====CommentController.editComment====");
        log.info("params[commentIdx: {}, commentReqDto: {}]", commentIdx, commentReqDto);
        CommentResDto editComment = commentService.editComment(commentIdx, User.builder().build(), commentReqDto);// todo login user
        model.addAttribute("editComment", editComment);
        log.info("model.addAttribute(\"comment\", comment);");
        log.info("editComment: {}", editComment);
        return editComment;
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/comments/{commentIdx}")
    public void deleteComment(@PathVariable final Long commentIdx) {
        log.info("====CommentController.deleteComment====");
        log.info("params[commentIdx: {}]", commentIdx);
        commentService.deleteComment(commentIdx, User.builder().build()); // todo login, author
        log.info("after commentService");
    }
}
