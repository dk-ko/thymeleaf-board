package com.example.demo.service;

import com.example.demo.common.model.PageRequest;
import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.dto.req.CommentReqDto;
import com.example.demo.dto.res.CommentResDto;
import com.example.demo.erros.UnauthorizedException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserRepository userRepository;

    /**
     * 댓글 생성
     * @param articleIdx 댓글 작성할 게시글
     * @param user 작성자
     * @param dto 댓글 내용(contents)
     * @return id, contents, updated date, user, article 정보
     * @exception UnauthorizedException 유저가 아닌 경우 // TODO security 개발 후 user 부분 확인(로그인한 사용자)
     */
    @Transactional
    public CommentResDto createComment(final Long articleIdx, final User user, final CommentReqDto dto) {
        // TODO login user check
        final Article foundArticle = articleService.findByIdx(articleIdx);
        Comment comment = dto.toEntity();
        comment.changeUser(testUser()); // todo user 변경
        comment.changeArticle(foundArticle);
        return commentRepository.save(comment).toResDto();
    }

    /**
     * 댓글 수정
     * @param commentIdx 수정할 댓글 idx
     * @param user 작성자(일치여부 확인)
     * @param dto 변경 내용(contents)
     * @return id, contents, updated date, user, article 정보
     * @exception UnauthorizedException 작성자, 관리자가 아닌 경우
     */
    @Transactional
    public CommentResDto editComment(final Long commentIdx, final User user, final CommentReqDto dto) {
        Comment foundComment = findByIdx(commentIdx);
        checkUser(foundComment, testUser());
//        checkUser(foundComment, user); // todo 작성자
        foundComment.editContents(dto.getContents());
        return commentRepository.save(foundComment).toResDto();
    }

    /**
     * 댓글 삭제
     * @param commentIdx 삭제할 댓글
     * @param user 작성자(일치여부 확인)
     * @exception UnauthorizedException 작성자, 관리자가 아닌 경우
     */
    @Transactional
    public void deleteComment(final Long commentIdx, final User user) {
        Comment foundComment = findByIdx(commentIdx);
        checkUser(foundComment, testUser()); // todo 작성자
//        checkUser(foundComment, user);
        commentRepository.deleteById(commentIdx);
    }

    /**
     * 댓글 조회(페이징, 10개씩, 등록순)
     * @param articleIdx 댓글 조회할 게시글
     * @param pageRequest 페이징 정보
     * @return 조회한 댓글들
     */
    @Transactional(readOnly = true)
    public Page<CommentResDto> getComments(final Long articleIdx, final PageRequest pageRequest) {
        log.info(pageRequest.toString());
        Article foundArticle = articleService.findByIdx(articleIdx);
        return commentRepository.findAllByArticle(foundArticle, pageRequest.of()).map(Comment::toResDto);
    }

    private Comment findByIdx(final Long commentIdx) {
        return commentRepository.findById(commentIdx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Comment Idx"));
    }
    private void checkUser(final Comment comment, final User user) {
        // TODO 관리자체크 추가
        if (!comment.getUser().equals(user)) throw new UnauthorizedException("User mismatch");
        // TODO ArticleService의 checkUser와 같은데 리팩토링할 수 있는 방법 없을까?
    }

    // todo security 이후 삭제
    private User testUser() { // todo security 구현 후 삭제
        return userRepository.findById(1L).get();
    }
}
