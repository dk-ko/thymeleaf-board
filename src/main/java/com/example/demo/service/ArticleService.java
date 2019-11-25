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
     * 게시글 1개 조회 (조회수 추가)
     * @param idx 조회할 게시글 idx
     * @return 조회한 게시글
     */
    @Transactional
    public ArticleResDto getArticle(final Long idx) {
        Article foundArticle = findByIdx(idx);
        foundArticle.addReadCnt(); // TODO 조회수는 제한 없이 할지, ip별로 구분해서 할지
        articleRepository.save(foundArticle);
        return foundArticle.toResDto();
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
    public ArticleResDto editArticle(final Long articleIdx, final User user, final ArticleUpdateReqDto dto) {
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
    public boolean deleteArticle(final Long articleIdx, final User user) {
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
    public ArticleResDto createArticle(final ArticleCreateReqDto dto, final User user, final Long boardIdx) {
        Board foundBoard = boardService.getBoard(boardIdx);
        Article article = dto.toEntity(user, foundBoard);
        article.changeUser(user);
        article.changeBoard(foundBoard);
        return articleRepository.save(article).toResDto();
    }

    /**
     * 게시글 목록 조회(페이징, 15개, 역순)
     * @param boardIdx 조회할 게시판
     * @param pageRequest 페이징 정보
     * @return 조회한 페이지의 게시글들
     */
    @Transactional(readOnly = true)
    public Page<ArticleListResDto> getArticlesByPageable(final Long boardIdx, final PageRequest pageRequest) {
        log.info(pageRequest.toString());
        Board foundBoard = boardService.getBoard(boardIdx);
        return articleRepository.findAllByBoard(foundBoard, pageRequest.of()).map(Article::toListResDto);
    }

    /**
     * 추천수 증가
     * @param articleIdx 추천수 증가시킬 게시글 idx
     * @exception UnauthorizedException 로그인 유저만 추천 가능
     */
    @Transactional
    public void addRecommendCnt(final Long articleIdx, final User user) {
        // TODO 로그인 유저 체크 추가, 중복 추천 체크 (로그인 유저 기준)
        findByIdx(articleIdx).addRecommendCnt();
    }

    private void checkUser(final Article article, final User user) {
        // TODO 관리자 체크 추가
        if (!article.getUser().equals(user)) throw new UnauthorizedException("User mismatch");
    }

    Article findByIdx(final Long idx) {
        return articleRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Article Idx"));
    }
}
