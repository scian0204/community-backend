package com.daelim.communitybackend.controller;

import com.daelim.communitybackend.dto.request.BoardApplyRequest;
import com.daelim.communitybackend.dto.request.BoardModifyRequest;
import com.daelim.communitybackend.dto.response.BoardResponse;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Board;
import com.daelim.communitybackend.service.BoardService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @GetMapping("/list")
    public Response<Page<Board>> getBoardList(@PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        return boardService.getBoardList(pageable);
    }

    @GetMapping("/listByRank")
    public Response<List<Object>> getBoardListByRank() {
        return boardService.getBoardListByRank();
    }

    @GetMapping("/{boardId}")
    public Response<BoardResponse> getBoardByBoardId(@PathVariable Integer boardId) {
        return boardService.getBoardByBoardId(boardId);
    }

    @GetMapping("/listByLike/{query}")
    public Response<Page<Board>> getListByLike(@PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String query) {
        return boardService.getListByLike(pageable, "%"+query+"%");
    }

    @GetMapping("/notAllowedList")
    public Response<Page<Board>>  getListByNotAllowed(@PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        return boardService.getListByNotAllowed(pageable);
    }

    @GetMapping("/listByUser/{userId}")
    public Response<Page<Board>>  getListByUser(@PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String userId) {
        return boardService.getListByUser(pageable, userId);
    }

    @PostMapping("/apply")
    public Response<Board> applyBoard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = BoardApplyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> boardObj) {
        return boardService.applyBoard(boardObj);
    }

    @PutMapping("/authorize/{boardId}")
    public Response<Boolean> authorize(@PathVariable Integer boardId, HttpSession session) {
        return boardService.authorize(boardId, session);
    }

    @PutMapping("/modify")
    public Response<Board> modifyBoard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = BoardModifyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> boardObj,
            HttpSession session
    ) {
        return boardService.modifyBoard(boardObj, session);
    }

    @DeleteMapping("/delete/{boardId}")
    public Response<Object> deleteBoard(@PathVariable Integer boardId) {
        return boardService.deleteBoard(boardId);
    }

}
