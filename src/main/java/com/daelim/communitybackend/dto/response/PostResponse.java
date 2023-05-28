package com.daelim.communitybackend.dto.response;

import com.daelim.communitybackend.entity.Post;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostResponse {
    private Integer postId;
    private Integer boardId;
    private String userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer recommend;
    private String image;
    private Timestamp writeDate;

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.boardId = post.getBoardId();
        this.userId = post.getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.recommend = post.getRecommend();
        this.image = post.getImage();
        this.writeDate = post.getWriteDate();
    }
}
