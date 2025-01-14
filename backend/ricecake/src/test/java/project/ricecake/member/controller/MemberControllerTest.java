package project.ricecake.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import project.ricecake.common.BaseResponse;
import project.ricecake.config.WebSecurityConfig;
import project.ricecake.error.exception.duplicate.UserDuplicateException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.error.exception.unauthorized.InvalidPasswordException;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.domain.response.PostLoginRes;
import project.ricecake.member.service.MemberService;
import project.ricecake.member.service.UserDetailsServiceImpl;
import project.ricecake.utils.JwtUtils;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Import({WebSecurityConfig.class})
@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @DisplayName("1. 회원가입 성공 테스트")
    @Test
    void signupSuccess() throws Exception {

        //given
        PostSignupReq signupReq = new PostSignupReq("member01", "qwer1234!", "test1", 10);
        BaseResponse<Object> baseResponse = BaseResponse.successResponse("MEMBER_001", true, "회원가입 성공", "ok");

        //when
        given(memberService.memberSignup(any(PostSignupReq.class))).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_001"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.result").value("ok"))
                .andDo(print());
    }

    @DisplayName("1-1. 회원 아이디 유효성 검증에 실패할 경우")
    @Test
    void signupFailInvalidMemberId() throws Exception {

        //given
        PostSignupReq signupReq = new PostSignupReq("test", "qwer1234!", "test1", 10);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("아이디 형식이 잘못되었습니다."))
                .andDo(print());
    }

    @DisplayName("1-2. 회원 비밀번호 유효성 검증에 실패할 경우")
    @Test
    void signupFailInvalidPw() throws Exception {
        //given
        PostSignupReq signupReq = new PostSignupReq("member01", "qw1", "test1", 10);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("비밀번호 형식이 잘못되었습니다."))
                .andDo(print());
    }

    @DisplayName("1-3. 회원 이름 유효성 검증에 실패할 경우")
    @Test
    void signupFailInvalidName() throws Exception {
        //given
        PostSignupReq signupReq = new PostSignupReq("member01", "qwer1234!", "t", 10);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("회원의 이름은 최소 2자부터 최대 20자까지 가능합니다."))
                .andDo(print());
    }

    @DisplayName("1-4. 회원 나이 유효성 검증에 실패할 경우")
    @Test
    void signupFailInvalidAge() throws Exception {
        //given
        PostSignupReq signupReq = new PostSignupReq("member01", "qwer1234!", "test1", 1);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("회원의 나이는 최소 10세부터 가능합니다."))
                .andDo(print());
    }

    @DisplayName("1-5. 중복된 회원이 있을 경우")
    @Test
    void signupFailDuplicateMember() throws Exception {

        //given
        PostSignupReq signupReq = new PostSignupReq("member01", "qwer1234!", "test1", 10);

        //when
        given(memberService.memberSignup(any(PostSignupReq.class))).willThrow(new UserDuplicateException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReq))
        );

        //then
        resultActions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.code").value("MEMBER_E002"))
                .andExpect(jsonPath("$.message").value("중복된 회원입니다."))
                .andDo(print());

    }

    @DisplayName("2. 로그인 성공 테스트")
    @Test
    void memberLogin() throws Exception {

        //given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZHgiOjExLCJtZW1iZXJJZCI6Im1lbWJlcjExIiwiaWF0IjoxNzM2ODM2MDc1LCJleHAiOjE3MzY4Mzk2NzV9.5GplE6_zcMmNqxm1WUwXv58OpURzKCDKB51FAb3iq1M";
        PostLoginReq loginReq = new PostLoginReq("member01", "qwer1234!");
        PostLoginRes loginRes = PostLoginRes.buildLoginRes(token);
        BaseResponse<Object> baseResponse = BaseResponse.successResponse("MEMBER_002", true, "로그인 성공", objectMapper.writeValueAsString(loginRes));

        //when
        given(memberService.memberLogin(any(PostLoginReq.class))).willReturn(baseResponse);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("MEMBER_002"))
                .andExpect(jsonPath("$.isSuccess").value("true"))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.result").value(objectMapper.writeValueAsString(loginRes)))
                .andDo(print());
    }

    @DisplayName("2-1. 로그인 아이디 유효성 검증에 실패할 경우")
    @Test
    void loginFailInvalidId() throws Exception {

        //given
        PostLoginReq loginReq = new PostLoginReq("test", "qwer1234!");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("아이디 형식이 잘못되었습니다."))
                .andDo(print());
    }

    @DisplayName("2-2. 로그인 비밀번호 유효성 검증에 실패할 경우")
    @Test
    void loginFailInvalidPw() throws Exception {

        //given
        PostLoginReq loginReq = new PostLoginReq("member01", "qw12");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.code").value("COMMON_E001"))
                .andExpect(jsonPath("$.message").value("비밀번호 형식이 잘못되었습니다."))
                .andDo(print());
    }

    @DisplayName("2-3. 찾을 수 없는 회원인 경우")
    @Test
    void loginFailNotFoundMember() throws Exception {

        //given
        PostLoginReq loginReq = new PostLoginReq("tester01", "qwer1234!");

        //when
        given(memberService.memberLogin(any(PostLoginReq.class))).willThrow(new UserNotFoundException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq))
        );

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.code").value("MEMBER_E001"))
                .andExpect(jsonPath("$.message").value("회원을 찾을 수 없습니다."))
                .andDo(print());
    }

    @DisplayName("2-4. 비밀번호가 틀린 경우")
    @Test
    void loginFailUnauthorizedPw() throws Exception {

        //given
        PostLoginReq loginReq = new PostLoginReq("member01", "qwer1234!@#");

        //when
        given(memberService.memberLogin(any(PostLoginReq.class))).willThrow(new InvalidPasswordException());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq))
        );

        //then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.code").value("MEMBER_E003"))
                .andExpect(jsonPath("$.message").value("비밀번호 틀립니다."))
                .andDo(print());
    }


}
