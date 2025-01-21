package project.ricecake.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.ricecake.error.ErrorCode;
import project.ricecake.member.domain.entity.MemberEntity;
import project.ricecake.member.repository.MemberRepository;

import java.util.Optional;

/**
 * UserDetailsServiceImpl
 * 인증 및 인가 작업을 커스텀 처리할 수 있게 UserDetailsService를 구현한 클래스
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 회원이 존재하는지 체크할 메서드
     * @param memberId (회원 아이디)
     * @return MemberEntity 객체
     * @throws UsernameNotFoundException : 찾으려는 회원이 없을 경우
     */
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Optional<MemberEntity> member = memberRepository.findByMemberId(memberId);

        if (member.isPresent()) {
            return member.get();
        } else {
            throw new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
}
