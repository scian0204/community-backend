package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.request.BoardModifyRequest;
import com.daelim.communitybackend.dto.response.BoardResponse;
import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Board;
import com.daelim.communitybackend.repository.BoardRepository;
import com.daelim.communitybackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BoardService {
    ObjectMapper objMpr = new ObjectMapper();
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;

    public Response<Page<Board>> getBoardList(Pageable pageable) {
        Response<Page<Board>> res = new Response<>();
        res.setData(boardRepository.findAllByIsAllowedIsTrue(pageable));

        return res;
    }

    public Response<List<Object>> getBoardListByRank() {
        Response<List<Object>> res = new Response<>();
        res.setData(boardRepository.getBoardsByLank());
        return res;
    }

    public Response<Board> applyBoard(Map<String, Object> boardObj) {
        Response<Board> res = new Response<>();
        Board board = objMpr.convertValue(boardObj, Board.class);

        Optional<Board> boardOptional = boardRepository.findBoardByBoardNameAndIsAllowedTrue(board.getBoardName());
        if (boardOptional.isPresent()) {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("게시판 이름이 중복됨");
            res.setError(error);
        } else {
            res.setData(boardRepository.save(board));
        }

        return res;
    }

    public Response<Boolean> authorize(Integer boardId, HttpSession session) {
        Response<Boolean> res = new Response<>();
        String userId = session.getAttribute("userId").toString();
        if (userId==null) {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("로그인 상태가 아님");
        } else {
            Boolean isAdmin = (boolean) session.getAttribute("isAdmin");
            if (!isAdmin) {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("관리자가 아님");
                res.setError(error);
            } else {
                Board board = boardRepository.getReferenceById(boardId);
                board.setIsAllowed(true);
                boardRepository.save(board);
                res.setData(true);
            }
        }
        return res;
    }

    public Response<Board> modifyBoard(Map<String, Object> boardObj, HttpSession session) {
        Response<Board> res = new Response<>();
        BoardModifyRequest boardModifyRequest = objMpr.convertValue(boardObj, BoardModifyRequest.class);
        Optional<Board> boardOptional = boardRepository.findById(boardModifyRequest.getBoardId());
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            if (board.getUserId().equals(session.getAttribute("userId"))) {
                board.setBoardName(boardModifyRequest.getBoardName());
                board.setIsAllowed(false);
                res.setData(boardRepository.save(board));
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시판을 제작한 유저와 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 아이디의 게시판이 없음");
            res.setError(error);
        }

        return res;
    }

    public Response<Object> deleteBoard(Integer boardId) {
        Response<Object> res = new Response<>();
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            boardRepository.deleteById(boardId);
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시판이 없음");
            res.setError(error);
        }
        return res;
    }

    public Response<BoardResponse> getBoardByBoardId(Integer boardId) {
        Response<BoardResponse> res = new Response<>();
        res.setData(new BoardResponse(boardRepository.getReferenceById(boardId)));
        return res;
    }

    public Response<Page<Board>> getListByLike(Pageable pageable, String query) {
        Response<Page<Board>> res = new Response<>();
        Page<Board> boards = boardRepository.findAllByIsAllowedIsTrueAndBoardNameLikeIgnoreCaseOrUserIdLike(pageable, query, query);
        res.setData(boards);

        return res;
    }

    public Response<Page<Board>> getListByNotAllowed(Pageable pageable) {
        Response<Page<Board>> res = new Response<>();
        Page<Board> boards = boardRepository.findAllByIsAllowedIsFalse(pageable);
        res.setData(boards);

        return res;
    }

    public Response<Page<Board>> getListByUser(Pageable pageable, String userId) {
        Response<Page<Board>> res = new Response<>();
        Page<Board> boards = boardRepository.findAllByUserIdAndIsAllowedIsTrue(pageable, userId);
        res.setData(boards);

        return res;
    }
}
