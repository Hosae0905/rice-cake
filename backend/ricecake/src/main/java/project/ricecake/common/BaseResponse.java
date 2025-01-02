package project.ricecake.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse<T> {

    private final String code;
    private final Boolean isSuccess;
    private final String message;
    private final T result;

    public static <T>BaseResponse<T> successResponse(String code, Boolean isSuccess, String message, T result) {
        return new BaseResponse<>(code, isSuccess, message, result);
    }

    // TODO: 추후 삭제
    public static <T>BaseResponse<T> failResponse(String code, Boolean isSuccess, String message, T result) {
        return new BaseResponse<>(code, isSuccess, message, result);
    }
}

