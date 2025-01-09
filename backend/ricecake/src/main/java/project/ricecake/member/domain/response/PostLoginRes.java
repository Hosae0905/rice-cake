package project.ricecake.member.domain.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLoginRes {
    private String token;

    public static PostLoginRes buildLoginRes(String token) {
        return PostLoginRes.builder()
                .token(token)
                .build();
    }
}
