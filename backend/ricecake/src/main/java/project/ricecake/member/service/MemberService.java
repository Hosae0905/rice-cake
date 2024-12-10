package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.Optional;

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

    public Object memberLogin(PostLoginReq postLoginReq) {
        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postLoginReq.getMemberId());

        if (findMember.isPresent()) {
            MemberEntity member = findMember.get();
            if (member.getMemberPw().equals(postLoginReq.getMemberPw())) {
                return "ok";
            } else {
                return "비밀번호가 틀립니다.";
            }
        }

        return "회원이 존재하지 않습니다.";
    }
}
