package project.ricecake.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ricecake.member.domain.entity.MemberEntity;

import java.util.Optional;

/**
 * MemberRepository
 * DB에 접근하여 관련 쿼리 작업을 진행하는 인터페이스
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    /**
     * 회원 아이디를 통해서 회원 정보를 찾는다.
     * @param memberId (회원 아이디)
     * @return Optional로 감싸진 MemberEntity 객체
     */
    Optional<MemberEntity> findByMemberId(String memberId);     
}
