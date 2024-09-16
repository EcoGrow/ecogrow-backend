package com.sw.ecogrowbackend.common.exception;

// 런타임 중 발생하는 예외 상황을 처리하기 위한 목적의 클래스

public class CustomException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}