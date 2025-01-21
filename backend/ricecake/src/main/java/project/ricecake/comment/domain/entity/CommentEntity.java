package project.ricecake.comment.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import project.ricecake.board.domain.entity.BoardEntity;
import project.ricecake.comment.domain.request.PostWriteCommentReq;
import project.ricecake.member.domain.entity.MemberEntity;

import java.time.LocalDateTime;

/**
 * CommentEntity
 * DB에 저장할 댓글 정보를 담을 클래스
 */
@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx", nullable = false)
    private Long commentIdx;

    // TODO: 추후 공부한 뒤 타입을 변경할 예정 (text | varchar)
    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "member_Idx")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "board_Idx")
    private BoardEntity board;

    /**
     * DB에 저장할 CommentEntity 객체를 생성
     * @param postWriteCommentReq (댓글 작성 요청 DTO)
     * @param member              (연관관계 주입을 위한 회원 객체)
     * @param board               (연관관계 주입을 위한 게시글 객체)
     * @return CommentEntity 객체
     */
    public static CommentEntity buildComment(PostWriteCommentReq postWriteCommentReq, MemberEntity member, BoardEntity board) {
        return CommentEntity.builder()
                .commentContent(postWriteCommentReq.getCommentContent())
                .member(member)
                .board(board)
                .build();
    }
}
