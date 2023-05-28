package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Comment;
import com.daelim.communitybackend.entity.Post;
import com.daelim.communitybackend.repository.CommentRepository;
import com.daelim.communitybackend.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {
    ObjectMapper objMpr = new ObjectMapper();
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;

    public Response<Page<Comment>> getCommentByPostId(Pageable pageable, Integer postId) {
        Response<Page<Comment>> res = new Response<>();
        Page<Comment> comments = commentRepository.findAllByPostId(pageable, postId);
        res.setData(comments);
        return res;
    }

    public Response<Comment> writeComment(Map<String, Object> commentObj) {
        Comment comment = objMpr.convertValue(commentObj, Comment.class);
        Optional<Post> postOptional = postRepository.findById(comment.getPostId());
        Response<Comment> res = new Response<>();
        if (postOptional.isPresent()) {
            res.setData(commentRepository.save(comment));
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시물이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Comment> modifyComment(Map<String, Object> commentObj) {
        Comment comment = objMpr.convertValue(commentObj, Comment.class);
        Optional<Comment> commentOptional = commentRepository.findById(comment.getPostId());
        Response<Comment> res = new Response<>();

        if (commentOptional.isPresent()) {
            if (postRepository.findById(comment.getPostId()).isPresent()) {
                comment.setWriteDate(commentOptional.get().getWriteDate());
                res.setData(commentRepository.save(comment));
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시물이 존재하지 않음");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 댓글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Object> deleteComment(Integer commentId, HttpSession session) {
        Response<Object> res = new Response<>();
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            if (commentOptional.get().getUserId().equals(session.getAttribute("userId"))) {
                commentRepository.delete(commentOptional.get());
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("로그인 된 아이디가 해당 댓글이 작성자와 일치하지 않음");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 댓글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }
}
