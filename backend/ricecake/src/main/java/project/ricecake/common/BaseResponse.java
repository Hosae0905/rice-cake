package project.ricecake.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse<T> {

    private String code;
    private Boolean isSuccess;
    private String message;
    private T result;

    public static <T>BaseResponse<T> successResponse(String code, Boolean isSuccess, String message, T result) {
        return new BaseResponse<>(code, isSuccess, message, result);
    }

    public static <T>BaseResponse<T> failResponse(String code, Boolean isSuccess, String message, T result) {
        return new BaseResponse<>(code, isSuccess, message, result);
    }
}

