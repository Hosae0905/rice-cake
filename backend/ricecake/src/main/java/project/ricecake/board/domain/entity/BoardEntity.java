package project.ricecake.board.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import project.ricecake.board.domain.request.PostCreateBoardReq;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_idx", nullable = false)
    private Long boardIdx;

    @Column(name = "board_title", nullable = false, length = 30)
    private String boardTitle;

    // TODO: text 타입에 대해서 공부 필요
    @Lob
    @Column(name = "board_content", nullable = false, length = 256)
    private String boardContent;

    @CreationTimestamp
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static BoardEntity buildBoard(PostCreateBoardReq postCreateBoardReq) {
        return BoardEntity.builder()
                .boardTitle(postCreateBoardReq.getBoardTitle())
                .boardContent(postCreateBoardReq.getBoardContent())
                .build();
    }
}
