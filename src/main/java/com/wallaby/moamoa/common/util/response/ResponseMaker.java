package com.wallaby.moamoa.common.util.response;

import com.wallaby.moamoa.common.dto.response.ResponseDto;
import com.wallaby.moamoa.common.exception.ErrorCode;

public class ResponseMaker {

    public static <T> ResponseDto<?> buildResponse(ErrorCode errorCode, T data) {
        return ResponseDto.<T>builder()
                .errorCode(errorCode.getCode())
                .msg(errorCode.getMsg())
                .data(data)
                .build();
    }
}
