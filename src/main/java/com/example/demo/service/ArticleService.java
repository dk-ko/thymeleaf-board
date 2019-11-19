package com.example.demo.service;

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
import org.springframework.data.domain.PageRequest;
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
     * @param idx 수정할 게시글 idx
     * @param dto 수정할 게시글 데이터(title, contents, updatedIp)
     * @return 수정한 게시글
     */
    @Transactional
    public ArticleResDto editArticle(Long idx, ArticleUpdateReqDto dto) {
        Article foundArticle = findByIdx(idx);
        foundArticle.editArticle(dto.getTitle(), dto.getContents(), dto.getUpdatedIp());
        return articleRepository.save(foundArticle).toResDto();
    }

    /**
     * 게시글 1개 삭제 (관계된 comment 전체 삭제)
     * @param idx 삭제할 게시글 idx
     */
    @Transactional
    public boolean deleteArticle(Long idx) {
        articleRepository.deleteById(idx);
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

    /** 게시글 목록 조회(페이징) **/
    // TODO 파라미터가 필요한지.. 프론트 개발하며 수정할 것.
    @Transactional(readOnly = true)
    public Page<Article> getArticlesByPageable(Pageable pageable) {
        pageable.getOffset();
        pageable.getPageSize();
        pageable.getPageNumber();

        log.info(pageable.toString());
        PageRequest pageRequest;
        return articleRepository.findAll(pageable);
    }

    private Article findByIdx(Long idx) {
        return articleRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Article Idx"));
    }
}
