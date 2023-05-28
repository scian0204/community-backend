package com.daelim.communitybackend.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String userId;
    private String userName;
    private String password;
    private String image;
}
