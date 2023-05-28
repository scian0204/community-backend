package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class PostWriteRequest {
    private Integer boardId;
    private String userId;
    private String title;
    private String content;
    private String image;
}
