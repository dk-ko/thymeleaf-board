package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.req.CommentReqDto;
import com.example.demo.dto.res.CommentResDto;
import com.example.demo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Api(value = "댓글")
@Controller
@AllArgsConstructor
@Slf4j
public class CommentController {
    private CommentService commentService;

    @ApiOperation("댓글 생성")
    @PostMapping(value = "/articles/{articleIdx}")
    public @ResponseBody CommentResDto createComment(@PathVariable final Long articleIdx, @RequestBody @Valid final CommentReqDto commentReqDto, Model model) {
        log.info("params[articleIdx: {}, commentReqDto: {}]", articleIdx, commentReqDto);
        CommentResDto comment = commentService.createComment(articleIdx, User.builder().build(), commentReqDto); // todo login user

        model.addAttribute("comment", comment);
        log.info("model.addAttribute(\"comment\", comment);");
        log.info("comment: {}", comment);
        return comment;
    }

}
