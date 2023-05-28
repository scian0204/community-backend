package com.daelim.communitybackend.dto.response;

import lombok.Data;

@Data
public class Response<E> {
    Error error = null;
    E data = null;
}
