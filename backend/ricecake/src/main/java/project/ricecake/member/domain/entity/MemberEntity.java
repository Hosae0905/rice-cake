package project.ricecake.member.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.comment.domain.entity.CommentEntity;
import project.ricecake.member.domain.request.PostSignupReq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MemberEntity
 * DB에 저장할 회원 정보를 담을 클래스
 */
@Entity
@Table(name = "member", indexes = @Index(name = "member_id_index", columnList = "member_id", unique = true))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "member_id", nullable = false, length = 30)
    private String memberId;

    @Column(name = "member_pw", nullable = false)
    private String memberPw;

    @Column(name = "member_name", nullable = false, length = 20)
    private String memberName;

    @Column(name = "member_age", nullable = false)
    private Integer memberAge;

    @CreationTimestamp
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "boardIdx", fetch = FetchType.LAZY)
    @Builder.Default
    private List<BoardEntity> boards = new ArrayList<>();

    @OneToMany(mappedBy = "commentIdx", fetch = FetchType.LAZY)
    @Builder.Default
    private List<CommentEntity> comments = new ArrayList<>();

    /**
     * DB에 저장할 MemberEntity 객체를 생성한다.
     * @param postSignupReq (회원가입 요청 DTO)
     * @param passwordEncoder (비밀번호를 암호화에 사용할 객체)
     * @return MemberEntity 객체
     */
    public static MemberEntity buildMember(PostSignupReq postSignupReq, PasswordEncoder passwordEncoder) {
        return MemberEntity.builder()
                .memberId(postSignupReq.getMemberId())
                .memberPw(passwordEncoder.encode(postSignupReq.getMemberPw()))
                .memberName(postSignupReq.getMemberName())
                .memberAge(postSignupReq.getMemberAge())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return memberPw;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
