package project.ricecake.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 기본 에러코드
    INVALID_INPUT_VALUE("COMMON_E001", "입력 값이 잘못되었습니다."),
    INTERNAL_SERVER_ERROR("COMMON_E002", "서버에서 오류가 발생하였습니다."),

    // Business 에러코드
    NOT_FOUND("DEFAULT_E001", "찾을 수 없는 데이터"),
    DUPLICATE("DEFAULT_E002", "중복된 데이터"),
    UNAUTHORIZED("DEFAULT_E003", "인증 실패"),

    // Member 관련 에러코드
    USER_NOT_FOUND("MEMBER_E001", "회원을 찾을 수 없습니다."),
    DUPLICATE_USER("MEMBER_E002", "중복된 회원입니다."),
    MISMATCH_PASSWORD("MEMBER_E003", "비밀번호 틀립니다."),

    // TODO: Board 관련 에러코드
    BOARD_NOT_FOUND("BOARD_E001", "게시글을 찾을 수 없습니다."),

    // TODO: Comment 관련 에러코드
    ;

    private final String errorCode;
    private final String message;
}
