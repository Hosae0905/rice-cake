package project.ricecake.member.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostLoginReq {
    private String memberId;
    private String memberPw;
}
