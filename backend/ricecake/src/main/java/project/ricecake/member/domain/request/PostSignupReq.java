package project.ricecake.member.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostSignupReq {
    private String memberId;
    private String memberPw;
    private String memberName;
    private Integer memberAge;
}
