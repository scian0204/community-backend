package com.daelim.communitybackend.controller;

import com.daelim.communitybackend.dto.request.CommentWriteRequest;
import com.daelim.communitybackend.dto.request.PostModifyRequest;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Comment;
import com.daelim.communitybackend.service.CommentService;
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
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/{postId}")
    public Response<Page<Comment>> getCommentByPostId(@PageableDefault(page = 0, size = 10, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Integer postId) {
        return commentService.getCommentByPostId(pageable, postId);
    }

    @PostMapping("/write")
    public Response<Comment> writeComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CommentWriteRequest.class)
                    )
            )
            @RequestBody Map<String, Object> commentObj) {
        return commentService.writeComment(commentObj);
    }

    @PutMapping("/modify")
    public Response<Comment> modifyComment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = PostModifyRequest.class)
                    )
            )
            @RequestBody Map<String, Object> commentObj) {
        return commentService.modifyComment(commentObj);
    }

    @DeleteMapping("/delete/{commentId}")
    public Response<Object> deleteComment(@PathVariable Integer commentId, HttpSession session) {
        return commentService.deleteComment(commentId, session);
    }
}
