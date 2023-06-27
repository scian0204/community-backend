package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class PostModifyRequest {
    private Integer postId;
    private String title;
    private String content;
    private String image;
}
