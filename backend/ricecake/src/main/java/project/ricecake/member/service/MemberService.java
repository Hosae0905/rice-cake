package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.common.BaseResponse;
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

        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postSignupReq.getMemberId());

        if (findMember.isPresent()) {
            return BaseResponse.failResponse("MEMBER_ERROR_001", false, "중복된 회원", "fail");
        } else {
            MemberEntity memberEntity = MemberEntity.buildMember(postSignupReq);
            if (memberEntity != null) {
                memberRepository.save(memberEntity);
                return BaseResponse.successResponse("MEMBER_001", true, "회원가입 성공", "ok");
            } else {
                return BaseResponse.failResponse("MEMBER_ERROR_002", false, "회원가입 실패", "fail");
            }
        }
    }

    public Object memberLogin(PostLoginReq postLoginReq) {
        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postLoginReq.getMemberId());

        if (findMember.isPresent()) {
            MemberEntity member = findMember.get();
            if (member.getMemberPw().equals(postLoginReq.getMemberPw())) {
                return BaseResponse.successResponse("MEMBER_002", true, "로그인 성공", "ok");
            } else {
                return BaseResponse.failResponse("MEMBER_ERROR_004", false, "비밀번호 틀림", "fail");
            }
        }

        return BaseResponse.failResponse("MEMBER_ERROR_003", false, "회원이 없음", "fail");
    }
}
