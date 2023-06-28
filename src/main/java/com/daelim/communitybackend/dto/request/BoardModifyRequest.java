package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class BoardModifyRequest {
    private Integer boardId;
    private String boardName;

}
