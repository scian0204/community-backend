package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.request.PostModifyRequest;
import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.PostResponse;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.Post;
import com.daelim.communitybackend.entity.RecmdUser;
import com.daelim.communitybackend.repository.PostRepository;
import com.daelim.communitybackend.repository.RecmdUserRepository;
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
    @Autowired
    RecmdUserRepository recmdUserRepository;

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

    public Response<Post> updatePost(Map<String, Object> postObj, HttpSession session) {
        Response<Post> res = new Response<>();
        PostModifyRequest postModifyRequest = objMpr.convertValue(postObj, PostModifyRequest.class);

        Optional<Post> postOptional = postRepository.findById(postModifyRequest.getPostId());
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getUserId().equals(session.getAttribute("userId"))) {
                post.setTitle(postModifyRequest.getTitle());
                post.setContent(postModifyRequest.getContent());
                post.setImage(postModifyRequest.getImage());
                res.setData(postRepository.save(post));
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시글의 작성자와 로그인된 아이디가 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 게시글이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<PostResponse> recmdPost(Integer postId, HttpSession session) {
        Response<PostResponse> res = new Response<>();
        String userId = session.getAttribute("userId").toString();
        
        Optional<RecmdUser> recmdUserOptional = recmdUserRepository.findByUserIdAndPostId(userId, postId);
        if (recmdUserOptional.isPresent()) {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("이미 추천 함");
            res.setError(error);
        } else {
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setRecommend(post.getRecommend()+1);
                res.setData(new PostResponse(postRepository.save(post)));
                RecmdUser recmdUser = new RecmdUser();
                recmdUser.setUserId(userId);
                recmdUser.setPostId(postId);
                recmdUserRepository.save(recmdUser);
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 게시글이 존재하지 않음");
                res.setError(error);
            }
        }
        
        return res;
    }

    public Response<PostResponse> deRecmdPost(Integer postId, HttpSession session) {
        Response<PostResponse> res = new Response<>();
        String userId = session.getAttribute("userId").toString();
        Post post = postRepository.getReferenceById(postId);
        post.setRecommend(post.getRecommend()-1);
        res.setData(new PostResponse(postRepository.save(post)));

        RecmdUser recmdUser = recmdUserRepository.findByUserIdAndPostId(userId, postId).get();
        recmdUserRepository.delete(recmdUser);


        return res;
    }

    public Response<Page<Post>> getListByLike(Pageable pageable, String query) {
        Response<Page<Post>> res = new Response<>();
        Page<Post> posts = postRepository.findAllByTitleLikeIgnoreCaseOrContentLikeIgnoreCaseOrUserIdLike(pageable, query, query, query);
        res.setData(posts);

        return res;
    }
}
