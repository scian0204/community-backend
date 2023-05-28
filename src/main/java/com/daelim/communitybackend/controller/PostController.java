package com.daelim.communitybackend.controller;

import com.daelim.communitybackend.dto.request.PostWriteRequest;
import com.daelim.communitybackend.dto.response.PostResponse;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Post;
import com.daelim.communitybackend.service.PostService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/list")
    public Response<Page<Post>> getPostList(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostList(pageable);
    }

    @GetMapping("/list/{boardId}")
    public Response<Page<Post>> getPostListByBoardId(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Integer boardId) {
        return postService.getPostListByBoardId(pageable, boardId);
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPostByPostId(@PathVariable Integer postId) {
        return postService.getPostByPostId(postId);
    }

    @PostMapping("/write")
    public Response<Post> writePost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostWriteRequest.class)
                    )
            )
            @RequestBody Map<String, Object> postObj) {
        return postService.writePost(postObj);
    }

    @DeleteMapping("/delete/{postId}")
    public Response<Object> deletePost(@PathVariable Integer postId, HttpSession session) {
        return postService.deletePost(postId, session);
    }
}
