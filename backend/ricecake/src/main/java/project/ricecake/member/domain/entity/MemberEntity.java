package project.ricecake.member.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.comment.domain.entity.CommentEntity;
import project.ricecake.member.domain.request.PostSignupReq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_idx", nullable = false)
    private Long memberIdx;

    @Column(name = "member_id", nullable = false, length = 30)
    private String memberId;

    @Column(name = "member_pw", nullable = false, length = 50)
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

    @OneToMany(mappedBy = "boardIdx")
    private List<BoardEntity> boards = new ArrayList<>();

    @OneToMany(mappedBy = "commentIdx")
    private List<CommentEntity> comments = new ArrayList<>();

    public static MemberEntity buildMember(PostSignupReq postSignupReq) {
        return MemberEntity.builder()
                .memberId(postSignupReq.getMemberId())
                .memberPw(postSignupReq.getMemberPw())
                .memberName(postSignupReq.getMemberName())
                .memberAge(postSignupReq.getMemberAge())
                .build();
    }
}
