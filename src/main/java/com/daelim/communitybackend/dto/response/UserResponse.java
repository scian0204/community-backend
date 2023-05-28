package com.daelim.communitybackend.dto.response;

import com.daelim.communitybackend.entity.User;
import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResponse {
    private String userId;
    private String userName;
    private String image;
    private Timestamp regDate;
    private Boolean isAdmin;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.image = user.getImage();
        this.regDate = user.getRegDate();
        this.isAdmin = user.getIsAdmin();
    }
}
