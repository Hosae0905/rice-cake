package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ricecake.common.BaseResponse;
import project.ricecake.error.exception.duplicate.UserDuplicateException;
import project.ricecake.error.exception.notfound.UserNotFoundException;
import project.ricecake.error.exception.unauthorized.InvalidPasswordException;
import project.ricecake.member.domain.request.PostLoginReq;
import project.ricecake.member.domain.request.PostSignupReq;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.domain.response.PostLoginRes;
import project.ricecake.member.repository.MemberRepository;
import project.ricecake.utils.JwtUtils;

import java.util.Optional;

/**
 * MemberServiceImpl
 * 회원과 관련된 기능들을 구현하기 위한 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;      // 비밀번호 암호화를 위한 의존성 주입
    private final JwtUtils jwtUtils;                    // JWT 기능을 사용하기 위한 의존성 주입

    /**
     * 회원가입 서비스 로직
     * @param postSignupReq (회원가입 요청 DTO)
     * @return 회원가입 성공 응답 객체(String)
     * @throws UserDuplicateException : 중복된 아이디가 있을 경우
     * @throws UserNotFoundException : 어떤 문제로 회원 생성이 안 될 경우
     */
    @Transactional
    @Override
    public BaseResponse<Object> memberSignup(PostSignupReq postSignupReq) {

        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postSignupReq.getMemberId());

        /**
         * 만약 회원이 존재하면 UserDuplicateException 예외를 던진다.
         * 그렇지 않으면 MemberEntity 객체를 생성한다.
         */
        if (findMember.isPresent()) {
            throw new UserDuplicateException();
        } else {
            MemberEntity memberEntity = MemberEntity.buildMember(postSignupReq, passwordEncoder);

            /**
             * 만약 MemberEntity가 정상적으로 생성되었을 경우 DB에 저장하고 성공 응답을 반환한다.
             * 그렇지 않을 경우 UserNotFoundException 예외를 던진다.
             */
            if (memberEntity != null) {
                memberRepository.save(memberEntity);
                return BaseResponse.successResponse("MEMBER_001", true, "회원가입 성공", "ok");
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * 로그인 서비스 로직
     * @param postLoginReq (로그인 요청 DTO)
     * @return 로그인 성공 응답 객체(PostLoginRes)
     * @throws InvalidPasswordException : 비밀번호가 일치하지 않을 경우
     * @throws UserNotFoundException : 회원을 찾을 수 없을 경우
     */
    @Override
    public BaseResponse<Object> memberLogin(PostLoginReq postLoginReq) {
        Optional<MemberEntity> findMember = memberRepository.findByMemberId(postLoginReq.getMemberId());

        /**
         * 만약 회원이 존재할 경우 MemberEntity 객체를 가져온다.
         * 그렇지 않을 경우 UserNotFoundException 예외를 던진다.
         */
        if (findMember.isPresent()) {
            MemberEntity member = findMember.get();

            /**
             * 만약 요청으로 받은 비밀번호와 MemberEntity 객체의 비밀번호가 같으면 JWT 토큰을 생성하고 성공 응답을 반환한다.
             * 그렇지 않을 경우 InvalidPasswordException 예외를 던진다.
             */
            if (passwordEncoder.matches(postLoginReq.getMemberPw(), member.getPassword())) {
                String token = jwtUtils.generateToken(member.getMemberIdx(), member.getMemberId());
                return BaseResponse.successResponse("MEMBER_002", true, "로그인 성공", PostLoginRes.buildLoginRes(token));
            } else {
                throw new InvalidPasswordException();
            }
        }

        throw new UserNotFoundException();
    }
}
