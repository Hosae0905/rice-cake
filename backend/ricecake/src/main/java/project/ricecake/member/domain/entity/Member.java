package project.ricecake.member.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member {
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

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
