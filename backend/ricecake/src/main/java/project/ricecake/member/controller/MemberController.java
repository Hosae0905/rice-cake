package project.ricecake.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.ricecake.common.BaseResponse;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.service.MemberService;

/**
 * MemberController - v1
 * 클라이언트의 회원에 관한 요청을 받는 컨트롤러 클래스
 * 기능 목록: 회원가입, 로그인
 */
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * @param postSignupReq (회원가입 요청 DTO)
     * @return 회원가입 성공 응답
     */
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<BaseResponse<Object>> memberSignup(@RequestBody @Valid PostSignupReq postSignupReq) {
        return ResponseEntity.ok().body(memberService.memberSignup(postSignupReq));
    }

    /**
     * 로그인 API
     * @param postLoginReq (로그인 요청 DTO)
     * @return 로그인 성공 응답
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<BaseResponse<Object>> memberLogin(@RequestBody @Valid PostLoginReq postLoginReq) {
        return ResponseEntity.ok().body(memberService.memberLogin(postLoginReq));
    }
}
