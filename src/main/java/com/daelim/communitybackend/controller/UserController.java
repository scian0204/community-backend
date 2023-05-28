package com.daelim.communitybackend.controller;

import com.daelim.communitybackend.dto.request.SignUpRequest;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.dto.response.UserResponse;
import com.daelim.communitybackend.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public Response<UserResponse> getUserInfo(@PathVariable String userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/logout")
    public Response<Object> logout(HttpSession session) {
        Response<Object> res = new Response<>();
        session.removeAttribute("userId");
        return res;
    }

    @GetMapping("/isidDup/{userId}")
    public Response<Boolean> isIdDup(@PathVariable String userId) {
        return userService.isIdDup(userId);
    }

    @GetMapping("/isLogin")
    public Response<Object> isLogin(HttpSession session) {
        Response<Object> res = new Response<>();
        res.setData(session.getAttribute("userId"));
        return res;
    }

    @PostMapping("/signUp")
    public Response<UserResponse> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = SignUpRequest.class)
                    )
            )
            @RequestBody Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.signUp(userObj, session);
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.login(userObj, session);
    }

    @PutMapping("/update")
    public Response<UserResponse> updateUserInfo(@RequestBody Map<String, Object> userObj, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.updateUserInfo(userObj, session);
    }

    @DeleteMapping("/delete/{userId}")
    public Response<Object> deleteUser(@PathVariable String userId, HttpSession session) {
        return userService.deleteUser(userId, session);
    }
}
