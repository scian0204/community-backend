package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.request.LoginRequest;
import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.dto.response.UserResponse;
import com.daelim.communitybackend.entity.User;
import com.daelim.communitybackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    ObjectMapper objMpr = new ObjectMapper();

    @Autowired
    UserRepository userRepository;

    public Response<UserResponse> getUserInfo(String userId) {
        Response<UserResponse> res = new Response<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            res.setData(new UserResponse(userOptional.get()));
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 아이디가 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<UserResponse> signUp(Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user = objMpr.convertValue(userObj, User.class);
        Response<UserResponse> res = new Response<>();

        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isPresent()) {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 아이디가 이미 존재함");
            res.setError(error);
        } else {
            user.setPassword(encrypt(user.getPassword()));
            res.setData(new UserResponse(userRepository.save(user)));
            session.setAttribute("userId", user.getUserId());
        }

        return res;
    }

    public Response<UserResponse> updateUserInfo(Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user = objMpr.convertValue(userObj, User.class);
        Response<UserResponse> res = new Response<>();
        Error error = new Error();
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isEmpty()){
            error.setErrorId(0);
            error.setMessage("해당 아이디가 존재하지 않음");
            res.setError(error);
        } else if (session.getAttribute("userId") == null) {
            error.setErrorId(1);
            error.setMessage("로그인 상태가 아님");
            res.setError(error);
        } else if (session.getAttribute("userId").equals(user.getUserId())) {
            error.setErrorId(2);
            error.setMessage("로그인 된 아이디가 아님");
            res.setError(error);
        } else {
            User currentUser = userOptional.get();
            user.setRegDate(currentUser.getRegDate());
            user.setPassword(encrypt(user.getPassword()));
            res.setData(new UserResponse(userRepository.save(user)));
        }

        return res;
    }

    public Response<Object> deleteUser(String userId, HttpSession session) {
        Optional<User> userOptional = userRepository.findById(userId);
        Response<Object> res = new Response<>();
        Error error = new Error();
        if (userOptional.isEmpty()){
            error.setErrorId(0);
            error.setMessage("해당 아이디가 존재하지 않음");
            res.setError(error);
        } else if (session.getAttribute("userId") == null) {
            error.setErrorId(1);
            error.setMessage("로그인 상태가 아님");
            res.setError(error);
        } else if (session.getAttribute("userId").equals(userId)) {
            error.setErrorId(2);
            error.setMessage("로그인 된 아이디가 아님");
            res.setError(error);
        } else {
            userRepository.delete(userOptional.get());
            session.removeAttribute("userId");
        }

        return res;
    }


    public Response<String> login(Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Response<String> res = new Response<>();
        Error error = new Error();
        LoginRequest loginRequest = objMpr.convertValue(userObj, LoginRequest.class);
        loginRequest.setPassword(encrypt(loginRequest.getPassword()));
        Optional<User> userOptional = userRepository.findById(loginRequest.getUserId());

        if (userOptional.isEmpty()) {
            error.setErrorId(0);
            error.setMessage("해당 아이디가 존재하지 않음");
            res.setError(error);
        } else if (!userOptional.get().getPassword().equals(loginRequest.getPassword())) {
            error.setErrorId(1);
            error.setMessage("비밀번호가 다름");
            res.setError(error);
        } else {
            session.setAttribute("userId", loginRequest.getUserId());
            res.setData(loginRequest.getUserId());
        }

        return res;
    }

    public Response<Boolean> isIdDup(String userId) {
        Boolean result = false;
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            result = true;
        }
        Response<Boolean> res = new Response<>();
        res.setData(result);
        return res;
    }

    public String encrypt(String pw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pw.getBytes("utf-8"));
        return bytesToHex(md.digest());
    }
    public String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
