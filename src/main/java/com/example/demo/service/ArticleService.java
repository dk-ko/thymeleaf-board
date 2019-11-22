package com.example.demo.service;

import com.example.demo.common.model.PageRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.req.ArticleCreateReqDto;
import com.example.demo.dto.req.ArticleUpdateReqDto;
import com.example.demo.dto.res.ArticleListResDto;
import com.example.demo.dto.res.ArticleResDto;
import com.example.demo.erros.UnauthorizedException;
import com.example.demo.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {
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
     * @param user 작성자
     * @param dto 수정할 게시글 데이터(title, contents, updatedIp)
     * @return 수정한 게시글
     * @exception UnauthorizedException 작성자, 관리자가 아닌 경우
     */
    @Transactional
    public ArticleResDto editArticle(Long articleIdx, User user, ArticleUpdateReqDto dto) {
        Article foundArticle = findByIdx(articleIdx);
        // TODO user 데이터 가져오는 로직
        checkUser(foundArticle, user);
        foundArticle.editTitle(dto.getTitle());
        foundArticle.editContents(dto.getContents());
        foundArticle.editUpdatedIp(dto.getUpdatedIp());
        return articleRepository.save(foundArticle).toResDto();
    }

    /**
     * 게시글 1개 삭제 (관계된 comment 전체 삭제)
     * @param articleIdx 삭제할 게시글 idx
     * @param user 작성자
     * @exception UnauthorizedException 작성자, 관리자가 아닌 경우
     */
    @Transactional
    public boolean deleteArticle(Long articleIdx, User user) {
        // TODO user 데이터 가져오는 로직
        Article foundArticle = findByIdx(articleIdx);
        checkUser(foundArticle, user);
        articleRepository.deleteById(articleIdx);
        return true;
    }

    /**
     * 게시글 생성
     * @param dto title, contents, createIp
     * @param user 로그인한 사용자
     * @param boardIdx 작성할 게시판 idx
     * @return 생성한 게시글
     * @exception UnauthorizedException 유저가 아닌 경우 // TODO security 개발 후 user 부분 확인(로그인한 사용자)
     */
    @Transactional
    public ArticleResDto createArticle(ArticleCreateReqDto dto, User user, Long boardIdx) {
        Board foundBoard = boardService.getBoard(boardIdx);
        return articleRepository.save(dto.toEntity(user, foundBoard)).toResDto();
    }

    /**
     * 게시글 목록 조회(페이징)
     * @param boardIdx 조회할 게시판
     * @param pageRequest 페이징 정보
     * @return 조회한 페이지의 게시글들
     */
    @Transactional(readOnly = true)
    public Page<ArticleListResDto> getArticlesByPageable(Long boardIdx, PageRequest pageRequest) {
        log.info(pageRequest.toString());
        Board foundBoard = boardService.getBoard(boardIdx);
        return articleRepository.findAllByBoard(foundBoard, pageRequest.of()).map(Article::toListResDto);
    }

    private void checkUser(Article article, User user) {
        // TODO 관리자 체크 추가
        if (!article.getUser().equals(user)) throw new UnauthorizedException("User mismatch");
    }

    private Article findByIdx(Long idx) {
        return articleRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Article Idx"));
    }
}
