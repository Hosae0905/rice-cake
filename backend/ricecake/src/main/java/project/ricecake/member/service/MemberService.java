package project.ricecake.member.service;

import org.springframework.stereotype.Service;
import project.ricecake.common.BaseResponse;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.domain.response.PostLoginRes;

/**
 * 회원과 관련된 기능들을 모아놓은 인터페이스
 */
@Service
public interface MemberService {

    /**
     * 회원가입 기능
     * @param postSignupReq (회원가입 요청 DTO)
     * @return 회원가입 성공 응답 객체(String)
     */
    BaseResponse<String> memberSignup(PostSignupReq postSignupReq);

    /**
     * 로그인 기능
     * @param postLoginReq (로그인 요청 DTO)
     * @return 로그인 성공 응답 객체(PostLoginRes)
     */
    BaseResponse<PostLoginRes> memberLogin(PostLoginReq postLoginReq);

}
