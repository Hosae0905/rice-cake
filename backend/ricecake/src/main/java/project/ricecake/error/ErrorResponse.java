package project.ricecake.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final Integer status;
    private final String code;
    private final String message;

    public ErrorResponse(Integer status, ErrorCode errorCode) {
        this.status = status;
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(Integer status, ErrorCode errorCode, String message) {
        this.status = status;
        this.code = errorCode.getErrorCode();
        this.message = message;
    }
}
