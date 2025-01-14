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

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

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
