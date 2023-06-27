package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class CommentModifyRequest {
    private Integer commentId;
    private Integer postId;
    private String userId;
    private Integer target;
    private String comment;
}
