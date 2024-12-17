package project.ricecake.comment.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import project.ricecake.comment.domain.request.PostWriteCommentReq;

import java.time.LocalDateTime;

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

    public static CommentEntity buildComment(PostWriteCommentReq postWriteCommentReq) {
        return CommentEntity.builder()
                .commentContent(postWriteCommentReq.getCommentContent())
                .build();
    }
}
