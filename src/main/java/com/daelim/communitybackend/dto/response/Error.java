package com.daelim.communitybackend.dto.response;

import lombok.Data;

@Data
public class Error {
    Integer errorId;
    String message;
}
