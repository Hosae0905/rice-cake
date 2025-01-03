package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.duplicate.UserDuplicateException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.error.exception.unauthorized.InvalidPasswordException;
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
            throw new UserDuplicateException();
        } else {
            MemberEntity memberEntity = MemberEntity.buildMember(postSignupReq);
            if (memberEntity != null) {
                memberRepository.save(memberEntity);
                return BaseResponse.successResponse("MEMBER_001", true, "회원가입 성공", "ok");
            } else {
                throw new UserNotFoundException();
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
                throw new InvalidPasswordException();
            }
        }

        throw new UserNotFoundException();
    }
}
