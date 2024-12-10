package project.ricecake.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<Object> memberSignup(@RequestBody PostSignupReq postSignupReq) {
        return ResponseEntity.ok().body(memberService.memberSignup(postSignupReq));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<Object> memberLogin(@RequestBody PostLoginReq postLoginReq) {
        return ResponseEntity.ok().body(memberService.memberLogin(postLoginReq));
    }
}
