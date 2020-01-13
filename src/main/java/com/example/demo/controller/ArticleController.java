package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.dto.req.ArticleCreateReqDto;
import com.example.demo.dto.req.ArticleUpdateReqDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@Api(value = "Article(게시글)")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @ApiOperation("게시글 생성")
    @PostMapping("/boards/{boardIdx}")
    public ArticleResDto createArticle(@PathVariable final Long boardIdx, @RequestBody ArticleCreateReqDto articleCreateReqDto, Model model) {
        // todo test user

        log.info("====ArticleController.createArticle====");
        log.info("params[boardIdx: {}, articleCreateReqDto: {}]", boardIdx, articleCreateReqDto);

        String clientIP = getClientIP();
        log.info("clientIP: {}", clientIP);
        articleCreateReqDto.setCreatedIP(clientIP);

        ArticleResDto article = articleService.createArticle(articleCreateReqDto, User.builder().build(), boardIdx);
        model.addAttribute("article", article);
        return article;
    }

    @ApiOperation("게시글 삭제")
    @DeleteMapping("/articles/{articleIdx}")
    public Long deleteArticle(@PathVariable final Long articleIdx) {
        return articleService.deleteArticle(articleIdx, User.builder().build());
    }

    @ApiOperation("게시글 수정")
    @PutMapping("/articles/{articleIdx}")
    public String editArticle(@PathVariable final Long articleIdx, ArticleUpdateReqDto articleUpdateReqDto) {
        ArticleResDto editArticle = articleService.editArticle(articleIdx, User.builder().build(), articleUpdateReqDto);
        return "redirect:articles/" + editArticle.getArticleIdx(); // todo 수정한 '게시글 조회'로 이동
    }

    @ApiOperation("게시글 추천")
    @PutMapping("/articles/recommend/{articleIdx}")
    public Integer recommendArticle(@PathVariable final Long articleIdx) {
        log.info("articleController.recommendArticle");
        log.info("params[articleIdx: {}]", articleIdx);
        // todo check user
        return articleService.addRecommendCnt(articleIdx, User.builder().build());
    }

    private String getClientIP() {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

        String clientIp = req.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isEmpty(clientIp)||"unknown".equalsIgnoreCase(clientIp)) clientIp = req.getHeader("Proxy-Client-IP");
        if (StringUtils.isEmpty(clientIp)||"unknown".equalsIgnoreCase(clientIp)) clientIp = req.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isEmpty(clientIp)||"unknown".equalsIgnoreCase(clientIp)) clientIp = req.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isEmpty(clientIp)||"unknown".equalsIgnoreCase(clientIp)) clientIp = req.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isEmpty(clientIp)||"unknown".equalsIgnoreCase(clientIp)) clientIp = req.getRemoteAddr();
        return clientIp;
    }
}
