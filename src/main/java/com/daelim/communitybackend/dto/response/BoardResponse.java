package com.daelim.communitybackend.dto.response;

import com.daelim.communitybackend.entity.Board;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BoardResponse {
    private Integer boardId;
    private String boardName;
    private String userId;
    private Timestamp regDate;
    private Boolean isAllowed;

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.userId = board.getUserId();
        this.regDate = board.getRegDate();
        this.isAllowed = board.getIsAllowed();
    }
}
