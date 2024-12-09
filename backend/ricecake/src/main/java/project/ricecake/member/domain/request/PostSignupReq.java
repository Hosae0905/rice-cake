package project.ricecake.member.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostSignupReq {
    private String memberId;
    private String memberPw;
    private String memberName;
    private Integer memberAge;
}
