package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // TODO DTO로 수정(DTO가 필요한가?), 에러처리, validation

    /**
     * 게시판 종류 조회
     * @return 조회한 게시판 리턴
     */
    @Transactional(readOnly = true)
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    /**
     * 게시판 조회
     * @param idx 조회할 게시판 idx
     * @return 조회한 게시판
     */
    @Transactional(readOnly = true)
    public Board getBoard(Long idx) {
        return boardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Board Idx"));
    }

    /**
     * 게시판 생성
     * @param board 생성할 게시판 데이터
     * @return 생성한 게시판 리턴
     */
    @Transactional
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    /**
     * 게시판 수정 (이름 수정)
     * @param idx 수정할 게시판 idx
     * @param boardName 수정할 게시판 데이터
     * @return 수정한 게시판 리턴
     */
    @Transactional
    public Board editBoard(Long idx, String boardName) {
        Board foundBoard = boardRepository.findById(idx).orElseThrow(() -> new EntityNotFoundException("No Entity found for Board Idx"));
        foundBoard.editBoardName(boardName);
        return boardRepository.save(foundBoard);
    }

    /**
     * 게시판 삭제
     * @param idx 삭제할 게시판 idx
     * @return 삭제 여부 리턴
     */
    @Transactional
    public boolean deleteBoard(Long idx) {
        boardRepository.deleteById(idx);
        return true;
    }

}
