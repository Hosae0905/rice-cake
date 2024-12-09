package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Object memberSignup(PostSignupReq postSignupReq) {
        MemberEntity memberEntity = MemberEntity.buildMember(postSignupReq);

        if (memberEntity != null) {
            memberRepository.save(memberEntity);
            return "created";
        } else {
            return null;
        }
    }
}
