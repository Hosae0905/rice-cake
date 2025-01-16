package project.ricecake.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BaseResponse
 * 클라이언트에 반환할 결과 정보를 담은 응답 객체
 * @param <T> (결과 객체의 타입)
 */
@AllArgsConstructor
@Getter
public class BaseResponse<T> {

    private final String code;          // 응답 코드
    private final Boolean isSuccess;    // 성공 여부
    private final String message;       // 성공 메시지
    private final T result;             // 성공 결과

    /**
     * 성공 응답 메서드
     * @param code      (응답 코드)
     * @param isSuccess (성공 여부)
     * @param message   (성공 메시지)
     * @param result    (성공 결과)
     * @return 성공 정보를 담은 BaseResponse 객체
     * @param <T>       (결과 객체의 타입)
     */
    public static <T>BaseResponse<T> successResponse(String code, Boolean isSuccess, String message, T result) {
        return new BaseResponse<>(code, isSuccess, message, result);
    }
}

