package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class BoardApplyRequest {
    private String boardName;
    private String userId;
}
