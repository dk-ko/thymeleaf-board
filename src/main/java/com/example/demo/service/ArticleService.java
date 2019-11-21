package com.example.demo.service;

import com.example.demo.common.model.PageRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.req.ArticleCreateReqDto;
import com.example.demo.dto.req.ArticleUpdateReqDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
    // TODO 테스트 해야함
    private final ArticleRepository articleRepository;
    private final BoardService boardService;

    /**
     * 게시글 1개 조회
     * @param idx 조회할 게시글 idx
     * @return 조회한 게시글
     */
    @Transactional(readOnly = true)
    public ArticleResDto getArticle(Long idx) {
        return findByIdx(idx).toResDto();
    }

    /**
     * 게시글 1개 수정
     * @param articleIdx 수정할 게시글 idx
     * @param dto 수정할 게시글 데이터(title, contents, updatedIp)
     * @return 수정한 게시글
     */
    @Transactional
    public ArticleResDto editArticle(Long articleIdx, ArticleUpdateReqDto dto) { // TODO Add user
        Article foundArticle = findByIdx(articleIdx);
        foundArticle.editArticle(dto.getTitle(), dto.getContents(), dto.getUpdatedIp());
        return articleRepository.save(foundArticle).toResDto();
    }

    /**
     * 게시글 1개 삭제 (관계된 comment 전체 삭제)
     * @param articleIdx 삭제할 게시글 idx
     */
    @Transactional
    public boolean deleteArticle(Long articleIdx) { // TODO add user
        articleRepository.deleteById(articleIdx);
        return true;
    }

    /**
     * 게시글 생성
     * @param dto title, contents, createIp
     * @param user 작성자
     * @param boardIdx 작성할 게시판 idx
     * @return 생성한 게시글
     */
    @Transactional
    public ArticleResDto createArticle(ArticleCreateReqDto dto, User user, Long boardIdx) { // TODO security 개발 후 user 부분 확인, 수정
        Board foundBoard = boardService.getBoard(boardIdx);
        return articleRepository.save(
                Article.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .createdIP(dto.getCreatedIP())
                .userName(user.getName())
                .user(user)
                .board(foundBoard)
                .build()).toResDto();
    }

    /**
     * 게시글 목록 조회(페이징)
     * @param boardIdx 조회할 게시판
     * @param pageRequest 페이징 정보
     * @return 조회한 페이지의 게시글들
     */
    @Transactional(readOnly = true)
    public Page<Article> getArticlesByPageable(Long boardIdx, PageRequest pageRequest) {
        log.info(pageRequest.toString());
        Board foundBoard = boardService.getBoard(boardIdx);
        return articleRepository.findAllByBoard(foundBoard, pageRequest.of());
    }

    private Article findByIdx(Long idx) {
        return articleRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Article Idx"));
    }
}
