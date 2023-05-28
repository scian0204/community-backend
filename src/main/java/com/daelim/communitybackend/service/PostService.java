package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.PostResponse;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Post;
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
public class PostService {
    ObjectMapper objMpr = new ObjectMapper();
    @Autowired
    PostRepository postRepository;

    public Response<Page<Post>> getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        Response<Page<Post>> res = new Response<>();
        res.setData(posts);
        return res;
    }

    public Response<Post> writePost(Map<String, Object> postObj) {
        Post post = objMpr.convertValue(postObj, Post.class);
        Response<Post> res = new Response<>();

        res.setData(postRepository.save(post));
        return res;
    }

    public Response<Page<Post>> getPostListByBoardId(Pageable pageable, Integer boardId) {
        Response<Page<Post>> res = new Response<>();
        res.setData(postRepository.findAllByBoardId(pageable, boardId));

        return res;
    }

    public Response<PostResponse> getPostByPostId(Integer postId) {
        Response<PostResponse> res = new Response<>();
        Post post = postRepository.getReferenceById(postId);
        post.setViewCount(post.getViewCount()+1);
        res.setData(new PostResponse(postRepository.save(post)));
        return res;
    }

    public Response<Object> deletePost(Integer postId, HttpSession session) {
        Response<Object> res = new Response<>();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()){
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시물이 존재하지 않음");
            res.setError(error);
        } else if (!postOptional.get().getUserId().equals(session.getAttribute("userId"))) {
            Error error = new Error();
            error.setErrorId(1);
            error.setMessage("해당 게시물을 작성한 유저가 아님");
            res.setError(error);
        } else if (postOptional.isPresent()) {
            postRepository.delete(postOptional.get());
        }

        return res;
    }
}
