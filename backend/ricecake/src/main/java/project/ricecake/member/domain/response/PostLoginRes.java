package project.ricecake.member.domain.response;

import lombok.Builder;
import lombok.Getter;

/**
 * PostLoginRes
 * 클라이언트에게 로그인 응답 정보를 담을 DTO 클래스
 */
@Getter
@Builder
public class PostLoginRes {
    private String token;       // JWT 토큰 정보

    /**
     * PostLoginRes 객체를 생성한다.
     * @param token (JWT 토큰)
     * @return PostLoginRes 객체
     */
    public static PostLoginRes buildLoginRes(String token) {
        return PostLoginRes.builder()
                .token(token)
                .build();
    }
}
