package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.dto.req.BoardReqDto;
import com.example.demo.dto.res.BoardResDto;
import com.example.demo.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // TODO 에러처리, validation

    /**
     * 게시판 종류 조회
     * @return 조회한 게시판 리턴
     */
    @Transactional(readOnly = true)
    public List<BoardResDto> getBoardList() {
        return boardRepository.findAll().stream().map(Board::toResDto).collect(Collectors.toList());
    }

    /**
     * 게시판 조회
     * @param idx 조회할 게시판 idx
     * @return 조회한 게시판
     */
    @Transactional(readOnly = true)
    public BoardResDto getBoard(final Long idx) {
        return findByIdx(idx).toResDto();
    }

    /**
     * 게시판 생성
     * @param dto 생성할 게시판 데이터(게시판 이름)
     * @return 생성한 게시판 리턴
     */
    @Transactional
    public BoardResDto createBoard(final BoardReqDto dto) { // TODO request dto or String
        return boardRepository.save(dto.toEntity()).toResDto();
    }

    /**
     * 게시판 수정 (이름 수정)
     * @param idx 수정할 게시판 idx
     * @param dto 수정할 게시판 데이터 (이름)
     * @return 수정한 게시판 리턴
     */
    @Transactional
    public BoardResDto editBoard(final Long idx, final BoardReqDto dto) {
        Board foundBoard = findByIdx(idx);
        foundBoard.editBoardName(dto.getName());
        return boardRepository.save(foundBoard).toResDto();
    }

    /**
     * 게시판 삭제
     * @param idx 삭제할 게시판 idx
     * @return 삭제 여부 리턴
     */
    @Transactional
    public boolean deleteBoard(final Long idx) {
        boardRepository.deleteById(idx);
        return true;
    }

    public Board findByIdx(final Long boardIdx) {
        return boardRepository.findById(boardIdx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Board Idx"));
    }
}
